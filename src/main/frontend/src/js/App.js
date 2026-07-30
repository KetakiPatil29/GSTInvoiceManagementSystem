import '../css/App.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '../js/LoginPage';
import AddUserPage from './AddUserPage';
import InvoiceForm from '../js/InvoiceForm';
import Customers from '../js/Customers';
import Products from '../js/Products';
import HomePage from '../js/HomePage';
import ViewInvoice from './ViewInvoice';
import ProtectedRoute from '../js/ProtectedRoute';


function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
		<Route path="/" element={<LoginPage />} /> 
		<Route path="/Login" element={<LoginPage />} />
  
         

		<Route path="/HomePage" element={<HomePage />} />
          <Route path="/Customers" element={<ProtectedRoute><Customers /></ProtectedRoute>} />
          <Route path="/Products" element={<ProtectedRoute><Products /></ProtectedRoute>} />
          <Route path="/InvoiceForm" element={<ProtectedRoute><InvoiceForm /></ProtectedRoute>} />
          <Route path="/User" element={<ProtectedRoute><AddUserPage /></ProtectedRoute>} />
		  <Route path="/ViewInvoice" element={<ProtectedRoute><ViewInvoice /></ProtectedRoute>} />
		  
        </Routes>
      </div>
    </Router>
  );
}
export default App;

