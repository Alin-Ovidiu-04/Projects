import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode as jwt_decode } from "jwt-decode";
import "../CSS/LoginPage.css";

const LoginPage = ({ onLogin }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const credentials = { email, password };
      const response = await fetch("http://localhost:8081/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });

      if (response.ok) {
        const data = await response.json();
        const token = data.token;

        localStorage.setItem("token", token);
        const decodedToken = jwt_decode(token);
        
        if (decodedToken && decodedToken.role) {
          if (decodedToken.role === "user") {
            await navigate("/user");
          } else if (decodedToken.role === "admin") {
            await navigate("/admin");
          }
        } else {
          console.error("Role is missing in the token.");
        }

        if (onLogin) {
          onLogin();
        }

        console.log("Authentication successful. Token:", token);
      } else {
        setErrorMessage("Authentication failed. Invalid email or password!"); 
        console.error("Authentication failed");
      }
    } catch (error) {
      setErrorMessage("An error occurred. Please try again later!"); 
      console.error("Error during authentication:", error);
    }
  };

  return (
    <div className="login-container">
      <div className="login-form">
        <h1>Log In</h1>
        <div className="input-container">
          <input
            type="text"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder=" "
            required
          />
          <label htmlFor="email">Email</label>
        </div>
        <div className="input-container">
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder=" "
            required
          />
          <label htmlFor="password">Password</label>
        </div>
        {errorMessage && <div className="error-message">{errorMessage}</div>} {/* Afișare mesaj de eroare */}
        <button onClick={handleLogin}>Continue</button>
      </div>
    </div>
  );
};

export default LoginPage;