import React, { useState } from "react";
import '../css/SignUpPage.css';

function SignUpPage() {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    role: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Print payload to console 
    console.log("Payload being sent:", formData);
    // call your backend signup API
    const response = await fetch("http://localhost:8080/api/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    });
    const data = await response.json();
    //alert(data.message);

    if (response.ok) { 
      alert("User registered successfully!");
     } 
    else { alert("Error registering user"); 
      
    }
  };

  return (
    <div className="form-container"> {/* same wrapper as LoginPage */}
      <form className="form-box" onSubmit={handleSubmit}>
        <h2 className="form-title">Add User</h2>

        <input
          type="text"
          name="username"
          placeholder="Username"
          value={formData.username}
          onChange={handleChange}
          className="form-input"
          required
        />

        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          className="form-input"
          required
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          className="form-input"
          required
        />

        <select
          name="role"
          value={formData.role}
          onChange={handleChange}
          className="form-input"
        >
          <option value="">Select Role</option>
          <option value="ADMIN">Admin</option>
          <option value="ACCOUNTANT">Accountant</option>
          <option value="VIEWER">Viewer</option>
        </select>

        <button type="submit" className="form-button">
          Register
        </button>
      </form>
    </div>
  );
}

export default SignUpPage;
