import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../css/App.css';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import CloseIcon from '@mui/icons-material/Close';
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import { Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField } from '@mui/material';
const InvoiceForm = () => {
	const [productList, setProductList] = useState([]);
	const [selectedProducts, setSelectedProducts] = useState([]);
	const [products, setProducts] = useState([]);
	const [items, setItems] = useState([{ productId: '', quantity: '', unit_price: 0 }]);
	const [invoiceDate, setInvoiceDate] = useState(new Date());
	const [customers, setCustomers] = useState([]);
	const [selectedCustomerId, setSelectedCustomerId] = useState("");
	const [selectedCustomer, setSelectedCustomer] = useState({}); // ✅ should be an object
	const [date, setDate] = useState(new Date());
	const [savedInvoice, setSavedInvoice] = useState(null);
	const [viewOpen, setViewOpen] = useState(false);
	const [invoiceId, setInvoiceId] = useState(null);


	// Fetch all customers
	useEffect(() => {
		axios.get("http://localhost:8080/api/customers")
			.then(res => setCustomers(res.data))
			.catch(err => console.error("Error loading customers:", err));
	}, []);

	// Fetch all products
	useEffect(() => {
		axios.get('http://localhost:8080/api/products')
			.then(res => setProductList(res.data))
			.catch(err => console.error('Error fetching products:', err));
	}, []);

	// Fetch selected customer details
	useEffect(() => {
		if (selectedCustomerId) {
			axios.get(`http://localhost:8080/api/customers/${selectedCustomerId}`)
				.then(res => setSelectedCustomer(res.data))
				.catch(err => console.error("Error loading customer details:", err));
		}
	}, [selectedCustomerId]);

	const handleProductChange = (index, productId) => {
		const selected = productList.find(p => p.id.toString() === productId);
		const updated = [...items];
		updated[index] = {
			productId,
			quantity: 1,
			name: selected.name,
			unit_price: selected.unit_price,
			gst_rate: selected.gst_rate
		};
		setItems(updated);
	};

	const handleQuantityChange = (index, qty) => {
		const updated = [...items];
		updated[index].quantity = parseInt(qty);
		setItems(updated);
	};

	const addItem = () => {
		setItems([...items, { productId: '', quantity: '', unit_price: 0 }]);
	};

	const handleCreateInvoice = async () => {
		if (!selectedCustomer || Object.keys(selectedCustomer).length === 0) {
			alert("Please select a customer before creating the invoice.");
			return;
		}
		if (!items || items.length === 0 || items.every(item => !item.productId)) {
			alert("Please select at least one product before creating the invoice.");
			return;
		}
		if (items.some(item => item.quantity <= 0)) {
			alert("Quantity must be greater than zero for all selected products.");
			return;
		}
		setViewOpen(true);
		};
		
		const handleDownload = async () => {
		const invoicePayload = {
			customer_id: selectedCustomer.id,
			invoiceDate: date.toISOString(),
			status: "PAID",
			items: items.map(product => ({
				product_id: product.productId,
				name: product.name,
				amount: product.unit_price,
				quantity: product.quantity,
				rate: product.gst_rate
			}))
		};

		console.log("Invoice payload:", invoicePayload);

		try {
			const response = await fetch('http://localhost:8080/api/invoices', {
				method: "POST",
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify(invoicePayload)
			});

			const savedInvoice = await response.json();
			console.log("Invoice created:", savedInvoice);
			setSavedInvoice(savedInvoice);
			window.open(`http://localhost:8080/api/invoices/${savedInvoice.id}/pdf`, "_blank");
			//setViewOpen(false);
			window.location.reload();
		} catch (error) {
			console.error("Error creating invoice:", error);
			alert("Failed to create invoice.");
		}
	};

	const calculateTotals = () => {
	    let totalAmount = 0;
	    let totalGST = 0;

	    items.forEach(({ unit_price, quantity, gst_rate }) => {
	        const amount = unit_price * quantity;
	        const gst = (amount * gst_rate) / 100;
	        totalAmount += amount;
	        totalGST += gst;
	    });

	    return {
	        totalAmount: totalAmount.toFixed(2),
	        totalGST: totalGST.toFixed(2),
	        grandTotal: (totalAmount + totalGST).toFixed(2)
	    };
	};
	const handleDelete = (indexToRemove) => {
	  const confirmDelete = window.confirm("Are you sure you want to delete this product?");
	  if (confirmDelete) {
	    const updatedItems = items.filter((_, index) => index !== indexToRemove);
	    setItems(updatedItems);
	  }
	};
	
	const handleClear = () => {
	  if (window.confirm('Clear all invoice data and refresh?')) {
	    window.location.reload();
	  }
	};


	return (
		<div className="container mt-4">
			<h2>Invoice</h2>
			<div className="header">
				<p><strong>Bright <br></br>Traders</strong></p>
				<p><strong>Location :</strong> <br></br> Maharashtra</p>
				<p><strong>Date : </strong><br></br>
					<DatePicker 
						selected={invoiceDate}
						onChange={(date) => setInvoiceDate(date)}
						dateFormat="dd/MM/yyyy"
						placeholderText="Select invoice date"
						className="custom-datepicker"
					/>
				</p>

				<div className="c">
					<h3>Customer</h3>
					<select className="s" onChange={(e) => setSelectedCustomerId(e.target.value)}>
						<option value="">Select Customer</option>
						{customers.map(c => (
							<option key={c.id} value={c.id}>{c.name}</option>
						))}
					</select>

					{selectedCustomer.id && (
						<table className="c1">
							<p><strong>Id:</strong> {selectedCustomer.id}</p>
							<p><strong>Name:</strong> {selectedCustomer.name}</p>
							<p><strong>Gstin:</strong> {selectedCustomer.gstin}</p>
							<p><strong>State:</strong> {selectedCustomer.state}</p>
							<p><strong>Email:</strong> {selectedCustomer.email}</p>
							<p><strong>Phone:</strong> {selectedCustomer.phone}</p>
							<p><strong>Address:</strong> {selectedCustomer.address}</p>
						</table>
					)}
				</div>
			</div>

			<table className="table table-bordered">
				<thead>
					<tr>
						<th>Sr.No</th>
						<th>Products</th>
						<th>Price</th>
						<th>Quantity</th>
						<th>GST</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
					{items.map((item, index) => (
						<tr key={index}>
							<td>{index + 1}</td>
							<td>
								<select className="form-select" value={item.productId}
									onChange={e => handleProductChange(index, e.target.value)}>
									<option value="">Select Product</option>
									{productList.map(p => (
										<option key={p.id} value={p.id}>{p.name}</option>
									))}
								</select>
							</td>
							<td>
								<input type="text" className="form-control" value={item.unit_price || ''} readOnly />
							</td>
							<td>
								<input type="number" min="1" className="form-control" value={item.quantity}
									onChange={e => handleQuantityChange(index, e.target.value)} />
							</td>
							<td>
								<input type="text" className="form-control" value={item.gst_rate || ''} readOnly />
							</td>
							<td>				
							  <IconButton aria-label="remove" onClick={() => handleDelete(index)} disabled={!item.productId} sx={{ color: 'error.main' }}>
							    <RemoveIcon />
							  </IconButton>
							</td>
						</tr>
					))}
				</tbody>
			</table>

			<IconButton aria-label="add"  sx={{ color: 'green' }} onClick={addItem}>
			<AddIcon /> </IconButton>
			<br />
			<Button sx={{ ml: 3, mr: 3, mt: 5 }} variant="contained" color="secondary" onClick={handleClear}>Clear</Button>
			<Button sx={{ mt: 5 }} variant="contained" color="secondary" onClick={handleCreateInvoice} >Create Invoice</Button>

			<Dialog open={viewOpen} onClose={() => setViewOpen(false)} fullWidth
				maxWidth="xs" >
				<DialogTitle sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
				Invoice Created				
				<IconButton aria-label="close" onClick={() => setViewOpen(false)}
				    sx={{ color: 'grey.500' }}  >
				    <CloseIcon />
				  </IconButton>
				</DialogTitle>
				<DialogContent dividers>
					<TextField label="Message" value="Invoice created successfully!" fullWidth
						margin="dense" InputProps={{ readOnly: true }}
					/>
					{items.length > 0 && (
					       <div style={{ marginTop: '1rem' }}>
					           <TextField label="Total Amount" value={`₹${calculateTotals().totalAmount}`}
					               fullWidth margin="dense" InputProps={{ readOnly: true }}
					           />
					           <TextField label="Total GST"  value={`₹${calculateTotals().totalGST}`}
					               fullWidth  margin="dense" InputProps={{ readOnly: true }}
					           />
					           <TextField label="Grand Total" value={`₹${calculateTotals().grandTotal}`}
					               fullWidth margin="dense" InputProps={{ readOnly: true }}
					           />
					       </div>
					   )}
				</DialogContent>
				<DialogActions>
					<Button onClick={handleDownload} disabled={invoiceId !== null}
					 color="secondary">Download </Button>
					<Button onClick={() => setViewOpen(false)} color="primary">
						Edit </Button>
				</DialogActions>
			</Dialog>
		</div>
	);
};

export default InvoiceForm;
