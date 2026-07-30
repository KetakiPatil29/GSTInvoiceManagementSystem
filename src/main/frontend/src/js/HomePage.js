import React from 'react';
import { Link } from 'react-router-dom';
import '../css/HomePage.css';

const HomePage = () => {
	const userRole = localStorage.getItem("userRole"); // or from context/state
	console.log(localStorage.getItem("userRole")); // Should log "admin"

  return (
    <div className="home-container">
      <header>
        <h1>Smart Invoice Generator</h1>
        <p>Create and manage invoices effortlessly</p>
      </header>
	  <section className="quick-actions">
	          {(userRole === "admin" || userRole === "accountant") && (
	            <Link to="/InvoiceForm" className="action-card">Create Invoice</Link>
	          )}

	          {userRole === "admin" && (
	            <>
	              <Link to="/Customers" className="action-card">Manage Customers</Link>
	              <Link to="/Products" className="action-card">Manage Products</Link>
				  <Link to="/user" className="action-card">Mange user</Link>
	            </>
	          )}

	          {(userRole === "admin" || userRole === "viewer" || userRole === "accountant") && (
	            <Link to="/ViewInvoice" className="action-card">View Invoices</Link>
	          )}
	        </section>
      <footer>
        <p>© 2025 Invoice Manager App</p>
      </footer>
    </div>
  );
};
export default HomePage;


