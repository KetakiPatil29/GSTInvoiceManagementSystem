import React, { useState, useEffect } from "react";
import {
	Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
	Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle
} from "@mui/material";
import axios from "axios";

const Customers = () => {
	const [customers, setCustomers] = useState([]);
	const [deleteStatus, setDeleteStatus] = useState({});
	const [open, setOpen] = useState(false);
	const [currentCustomer, setCurrentCustomer] = useState({ name: "", gstin: "", state: "", email: "", phone: "", address: "" });
	const [isEditing, setIsEditing] = useState(false);
	const [viewOpen, setViewOpen] = useState(false);
	const [selectedCustomer, setSelectedCustomer] = useState(null);

	useEffect(() => {
		fetchCustomers();
	}, []);

	const fetchCustomers = async () => {
	  const response = await axios.get("http://localhost:8080/api/customers");
	  const data = response.data;
	  setCustomers(data);

	  const statusMap = {};
	  for (const customer of data) {
	    const res = await fetch(`http://localhost:8080/api/customers/${customer.id}/can-delete`);
	    const canDelete = await res.json();
	    statusMap[customer.id] = canDelete;
	  }
	  setDeleteStatus(statusMap);
	};


	const handleOpen = (customer = { name: "", gstin: "", state: "", email: "", phone: "", address: "" }) => {
		setCurrentCustomer(customer);
		setIsEditing(!!customer.id);
		setOpen(true);
	};

	const handleClose = () => {
		setOpen(false);
		setCurrentCustomer({ name: "", gstin: "", state: "", email: "", phone: "", address: "" });
	};

	const handleSave = async () => {
		if (isEditing) {
			await axios.put(`http://localhost:8080/api/customers/${currentCustomer.id}`, currentCustomer);
			alert("Customer Upadted Successfully");
		}
		else {
			await axios.post("http://localhost:8080/api/customers", currentCustomer);
			alert("Customer Create Successfully");
		}
		fetchCustomers();
		handleClose();
	};

	const handleView = (customer) => {
		setSelectedCustomer(customer);
		setViewOpen(true);
	};

	const handleDelete = async (id) => {
		try {
			await axios.delete(`http://localhost:8080/api/customers/${id}`);
			alert("Customer Deleted Successfully");
			fetchCustomers();
		}
		catch (error) {
			alert("If a customer has invoices then customer cannot deleted");
		}
	};

	return (
		<div>
			<h1 align="center">Customer Module</h1>
			<Button sx={{ ml: 5 }} variant="contained" color="secondary" onClick={() => handleOpen()}>
				Add Customer
			</Button>
			<TableContainer sx={{ pt: 5 }}>
				<Table>
					<TableHead>
						<TableRow>
							<TableCell align="center" sx={{ fontWeight: 'bold' }}>Name</TableCell>
							<TableCell align="center" sx={{ fontWeight: 'bold' }}>Email</TableCell>
							<TableCell align="center" sx={{ fontWeight: 'bold' }}>Actions</TableCell>
						</TableRow>
					</TableHead>
					<TableBody>
						{customers.map((customer) => (
							<TableRow key={customer.id}>
								<TableCell>{customer.name}</TableCell>
								<TableCell>{customer.email}</TableCell>
								<TableCell>
									<Button sx={{ ml: 3, mr: 3 }} variant="outlined" color="secondary" onClick={() => handleView(customer)}>View</Button>
									<Button sx={{ mr: 3 }} variant="outlined" color="primary" onClick={() => handleOpen(customer)}>Edit</Button>
									<Button variant="outlined" color="secondary" onClick={() => handleDelete(customer.id)}
									  disabled={!deleteStatus[customer.id]}
									  title={!deleteStatus[customer.id] ? "Cannot delete: invoices exist" : ""}>
									  Delete</Button>
								</TableCell>
							</TableRow>
						))}
					</TableBody>
				</Table>
			</TableContainer>

			<Dialog open={open} onClose={handleClose}>
				<DialogTitle>{isEditing ? "Edit Customer" : "Add Customer"}</DialogTitle>
				<DialogContent>
					<TextField label="Name" fullWidth margin="dense" value={currentCustomer.name}
						onChange={(e) => setCurrentCustomer({ ...currentCustomer, name: e.target.value })}
					/>
					<TextField label="GSTIN" fullWidth margin="dense" value={currentCustomer.gstin}
						onChange={(e) => setCurrentCustomer({ ...currentCustomer, gstin: e.target.value })}
					/>
					<TextField label="State" fullWidth margin="dense" value={currentCustomer.state}
						onChange={(e) => setCurrentCustomer({ ...currentCustomer, state: e.target.value })}
					/>
					<TextField label="Email" fullWidth margin="dense" value={currentCustomer.email}
						onChange={(e) => setCurrentCustomer({ ...currentCustomer, email: e.target.value })}
					/>
					<TextField label="Phone" fullWidth margin="dense" value={currentCustomer.phone}
						onChange={(e) => setCurrentCustomer({ ...currentCustomer, phone: e.target.value })}
					/>
					<TextField label="Address" fullWidth margin="dense" value={currentCustomer.address}
						onChange={(e) => setCurrentCustomer({ ...currentCustomer, address: e.target.value })}
					/>
				</DialogContent>

				<DialogActions>
					<Button onClick={handleClose}>Cancel</Button>
					<Button onClick={handleSave} color="primary">Save</Button>
				</DialogActions>
			</Dialog>


			<Dialog open={viewOpen} onClose={() => setViewOpen(false)}>
				<DialogTitle>View Customer</DialogTitle>
				<DialogContent dividers>
					<TextField label="Id" value={selectedCustomer?.id || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="Name" value={selectedCustomer?.name || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="GSTIN" value={selectedCustomer?.gstin || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="State" value={selectedCustomer?.state || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="Email" value={selectedCustomer?.email || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="Phone" value={selectedCustomer?.phone || ""} fullWidth margin="dense"
						InputProps={{ readOnly: true }}
					/>
					<TextField label="Address" value={selectedCustomer?.address || ""} fullWidth margin="dense"
						multiline InputProps={{ readOnly: true }}
					/>
				</DialogContent>
				<DialogActions>
					<Button onClick={() => setViewOpen(false)} color="primary">Close</Button>
				</DialogActions>
			</Dialog>
		</div>
	);
};

export default Customers;
