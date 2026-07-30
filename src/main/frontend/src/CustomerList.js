/*import { useEffect, useState } from "react";
import axios from "axios";
import './App.css';

const CustomerSelector = () => {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState("");
  const [selectedCustomer, setSelectedCustomer] = useState([]);

  // Fetch all customers for dropdown
  useEffect(() => {
    axios.get("http://localhost:8080/api/customers")
      .then(res => setCustomers(res.data))
      .catch(err => console.error("Error loading customers:", err));
  }, []);

  // Fetch selected customer details
  useEffect(() => {
    if (selectedCustomerId) {
      axios.get(`http://localhost:8080/api/customers/${selectedCustomerId}`)
        .then(res => setSelectedCustomer(res.data))
        .catch(err => console.error("Error loading customer details:", err));
    }
  }, [selectedCustomerId]);

  return (
    <div className="c">
	
      <h3>Customer</h3>
	  
      <select className="s" onChange={(e) => setSelectedCustomerId(e.target.value)}>
        <option value="">Select Customer</option>
        {customers.map(c => (
          <option key={c.id} value={c.id}>{c.name}</option>
        ))}
      </select>
	  
	  <table className= "c1">
	  <p><strong>Id :<br></br></strong> {selectedCustomer.id}</p>
	  <p><strong>Name :<br></br></strong> {selectedCustomer.name}</p>
	  <p><strong>Gstin :<br></br></strong> {selectedCustomer.gstin}</p>
	  <p><strong>State :<br></br></strong> {selectedCustomer.state}</p>
	  <p><strong>Email :<br></br></strong> {selectedCustomer.email}</p>
	  <p><strong>Phone :<br></br></strong> {selectedCustomer.phone}</p>
	  <p><strong>Address :<br></br></strong> {selectedCustomer.address}</p>
	  </table>
    </div>
  );
};

export default CustomerSelector;
*/