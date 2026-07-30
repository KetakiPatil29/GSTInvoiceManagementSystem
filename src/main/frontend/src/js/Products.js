import React, { useState, useEffect } from "react";
import {
	Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
	Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle
} from "@mui/material";
import axios from "axios";

const Products = () => {
	const [products, setProducts] = useState([]);
	const [deleteStatus, setDeleteStatus] = useState({});
	const [open, setOpen] = useState(false);
	const [currentProduct, setCurrentProduct] = useState({
		name: "", description: "", hsn_code: "", gst_rate: "", unit_price: ""
	});
	const [isEditing, setIsEditing] = useState(false);
	const [viewOpen, setViewOpen] = useState(false);
	const [selectedProduct, setSelectedProduct] = useState(null);

	useEffect(() => {
		fetchProducts();
	}, []);


	  const fetchProducts = async () => {
	    const response = await axios.get("http://localhost:8080/api/products");
	    const data = response.data;
	    setProducts(data);

	    const statusMap = {};
	    for (const product of data) {
	      const res = await fetch(`http://localhost:8080/api/products/${product.id}/can-delete`);
	      const canDelete = await res.json();
	      statusMap[product.id] = canDelete;
	    }
	    setDeleteStatus(statusMap);
	  };


	const handleOpen = (product = {
		name: "", description: "", hsn_code: "", gst_rate: "", unit_price: ""
	}) => {
		setCurrentProduct(product);
		setIsEditing(!!product.id);
		setOpen(true);
	};

	const handleClose = () => {
		setOpen(false);
		setCurrentProduct({
			name: "", description: "", hsn_code: "", gst_rate: "", unit_price: ""
		});
	};

	const handleSave = async () => {
		if (isEditing) {
			await axios.put(`http://localhost:8080/api/products/${currentProduct.id}`, currentProduct);
			alert("Products Upadted Successfully");
		}
		else {
			await axios.post("http://localhost:8080/api/products", currentProduct);
			alert("Products Create Successfully");
		}
		fetchProducts();
		handleClose();
	};

	const handleView = (product) => {
		setSelectedProduct(product);
		setViewOpen(true);
	};

	const handleDelete = async (id) => {
		try {
			await axios.delete(`http://localhost:8080/api/products/${id}`);
			alert("Products Deleted Successfully");
			fetchProducts();
		}
		catch (error) {
			alert("If a products has invoices then products cannot deleted");
		}
	};

	return (
		<div>
			<h1 align="center">Products Module</h1>
			<Button sx={{ ml: 5 }} variant="contained" color="secondary" onClick={() => handleOpen()}>
				Add products
			</Button>
			<TableContainer sx={{ pt: 5 }}>
				<Table>
					<TableHead>
						<TableRow>
							<TableCell align="center" sx={{ fontWeight: 'bold' }}>Name</TableCell>
							<TableCell align="center" sx={{ fontWeight: 'bold' }}>Price</TableCell>
							<TableCell align="center" sx={{ fontWeight: 'bold' }}>Actions</TableCell>
						</TableRow>
					</TableHead>
					<TableBody>
						{products.map((product) => (
							<TableRow key={product.id}>
								<TableCell>{product.name}</TableCell>
								<TableCell>₹ {product.unit_price}</TableCell>
								<TableCell>
									<Button sx={{ ml: 3, mr: 3 }} variant="outlined" color="secondary" onClick={() => handleView(product)}>View</Button>
									<Button sx={{ mr: 3 }} variant="outlined" color="primary" onClick={() => handleOpen(product)}>Edit</Button>
									<Button variant="outlined" color="secondary" onClick={() => handleDelete(product.id)}
									  disabled={!deleteStatus[product.id]}
									  title={!deleteStatus[product.id] ? "Cannot delete: linked to invoice" : ""}
									>
									  Delete
									</Button>
								</TableCell>
							</TableRow>
						))}
					</TableBody>
				</Table>
			</TableContainer>

			<Dialog open={open} onClose={handleClose}>
				<DialogTitle>{isEditing ? "Edit Products" : "Add Products"}</DialogTitle>
				<DialogContent>
					<TextField label="Name" fullWidth margin="dense" value={currentProduct.name}
						onChange={(e) => setCurrentProduct({ ...currentProduct, name: e.target.value })}
					/>
					<TextField label="Description" fullWidth margin="dense" value={currentProduct.description}
						onChange={(e) => setCurrentProduct({ ...currentProduct, description: e.target.value })}
					/>
					<TextField label="HSN Code" fullWidth margin="dense" value={currentProduct.hsn_code}
						onChange={(e) => setCurrentProduct({ ...currentProduct, hsn_code: e.target.value })}
					/>
					<TextField label="GST Rate" fullWidth margin="dense" value={currentProduct.gst_rate}
						onChange={(e) => setCurrentProduct({ ...currentProduct, gst_rate: e.target.value })}
					/>
					<TextField label="Unit Price" fullWidth margin="dense" value={currentProduct.unit_price}
						onChange={(e) => setCurrentProduct({ ...currentProduct, unit_price: e.target.value })}
					/>
				</DialogContent>

				<DialogActions>
					<Button onClick={handleClose}>Cancel</Button>
					<Button onClick={handleSave} color="primary">Save</Button>
				</DialogActions>
			</Dialog>

			<Dialog open={viewOpen} onClose={() => setViewOpen(false)}>
				<DialogTitle>View Products</DialogTitle>
				<DialogContent dividers>
					<TextField label="Id" value={selectedProduct?.id || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="Name" value={selectedProduct?.name || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="Description" value={selectedProduct?.description || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="hsn_code" value={selectedProduct?.hsn_code || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="gst_rate" value={selectedProduct?.gst_rate || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="unit_price" value={selectedProduct?.unit_price || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
				</DialogContent>
				<DialogActions>
					<Button onClick={() => setViewOpen(false)} color="primary">Close</Button>
				</DialogActions>
			</Dialog>
		</div>
	);
};

export default Products;
