import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import '../css/LoginPage.css';

const LoginPage = () => {

	const [email, setEmail] = useState("");
	  const [password, setPassword] = useState("");
	  const [role, setRole] = useState("");
	  const navigate = useNavigate();

	  const handleLogin = async (e) => {
		e.preventDefault(); 
		const payload = {
		  email: email.trim(),
		  password: password.trim(),
		  role: role.trim() // if roles are lowercase in DB
		};
		console.log("Invoice payload:", payload);
	    try {
	      const res = await axios.post("http://localhost:8080/api/login", { email, password, role });
		  console.log("Full response:", res);

	      if (res.status === 200 ) {
			localStorage.setItem("userRole", role.toLowerCase());
			alert("Login successful");
	        navigate("/HomePage");
	      }
	    } catch (err) {
	      alert("Invalid credentials");
	    }
	  };
	
  return (
    <div className="login-container">
      <h2>Login to Invoice Manager</h2>
      <form onSubmit={handleLogin}>
        <input type="email" placeholder="Email" value={email}
          onChange={(e) => setEmail(e.target.value)} required
        />
        <input type="password" placeholder="Password" value={password}
          onChange={(e) => setPassword(e.target.value)} required
        />
		<select value={role} onChange={(e) => setRole(e.target.value)} required
		 style={{ height: '50px'}}>
		  <option value="">Select Role</option>
		  <option value="admin">Admin</option>
		  <option value="accountant">Accountant</option>
		  <option value="viewer">Viewer</option>
		</select>
        <button type="submit" style={{ marginTop: '30px' }}>Login</button>
      </form>
	  <br></br>
	  {/* <a href="SignUpPage">Sign Up</a> */}
    </div>
  );
};
export default LoginPage;