import React, { useEffect, useState } from 'react';
import {
  Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow, Button
} from '@mui/material';
import axios from 'axios';

const ViewInvoice = () => {
 const [invoices, setInvoices] = useState([]);


 useEffect(() => {
   axios.get('http://localhost:8080/api/invoices')
     .then(response => {
       console.log('Fetched data:', response.data); // should be an array
       setInvoices(response.data); // ✅ use the array directly
     })
     .catch(error => 
       console.error('Error fetching invoices:', error));
 }, []);

  return (
	<div>
	<h1 align="center">Invoice Module</h1>
    <TableContainer sx={{ pt: 5 }}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell align="center" sx={{ fontWeight: 'bold' }}>Invoice ID</TableCell>
            <TableCell align="center" sx={{ fontWeight: 'bold' }}>Invoice Number</TableCell>
            <TableCell align="center" sx={{ fontWeight: 'bold' }}>Customer ID</TableCell>
			<TableCell align="center" sx={{ fontWeight: 'bold' }}>Customer Name</TableCell>
            <TableCell align="center" sx={{ fontWeight: 'bold' }}>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
        {Array.isArray(invoices) && invoices.map((invoice) => (
            <TableRow key={invoice.id}>
              <TableCell>{invoice.id}</TableCell>
              <TableCell>{invoice.invoice_number}</TableCell>
              <TableCell>{invoice.customer?.id}</TableCell>
			  <TableCell>{invoice.customer?.name}</TableCell>
              <TableCell align="center">
               <Button sx={{ ml: 3, mr: 3 }} variant="outlined" color="secondary" 
				 onClick={() => window.open(`http://localhost:8080/api/invoices/${invoice.id}/pdf`, '_blank')}>View</Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
	</div>
  );
};

export default ViewInvoice;
