import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../CSS/SignupPage.css";

const SignupPage = () => {
  const [first_name, setFirstName] = useState("");
  const [last_name, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [height, setHeight] = useState("");
  const [weight, setWeight] = useState("");
  const [age, setAge] = useState("");
  const [activity_level, setActivityLevel] = useState("Medium");
  const [errorMessage, setErrorMessage] = useState(""); // Stare pentru mesajul de eroare
  const navigate = useNavigate();

  const handleSignin = async (event) => {
    event.preventDefault();
    
    const userData = {
      id_role: 1,
      first_name,
      last_name,
      email,
      password,
      height,
      weight,
      age,
      activity_level,
    };
    
    try {
      const response = await fetch("http://localhost:8081/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        console.log("Register succes!");
        navigate("/login");
      } else {
        setErrorMessage("Register error. Please check the entered details."); // setez mesajul de eroare
        console.error("Register error:", response.statusText);
      }
    } catch (error) {
      setErrorMessage("An error occurred during registration. Please try again later."); // Setează mesajul de eroare
      console.error("Register error:", error.message);
    }
  };

  return (
    <div className="signup-container">
      <div className="signup-form">
        <h1>Sign Up</h1>
        <form onSubmit={handleSignin}>
          <div className="input-container">
            <input
              type="text"
              id="firstname"
              value={first_name}
              onChange={(e) => setFirstName(e.target.value)}
              placeholder=" "
              required
            />
            <label htmlFor="firstname">First Name</label>
          </div>
          <div className="input-container">
            <input
              type="text"
              id="lastname"
              value={last_name}
              onChange={(e) => setLastName(e.target.value)}
              placeholder=" "
              required
            />
            <label htmlFor="lastname">Last Name</label>
          </div>
          <div className="input-container">
            <input
              type="email"
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
          <div className="input-container">
            <input
              type="number"
              id="height"
              value={height}
              onChange={(e) => setHeight(e.target.value)}
              placeholder=" "
              required
            />
            <label htmlFor="height">Height(cm)</label>
          </div>
          <div className="input-container">
            <input
              type="number"
              id="weight"
              value={weight}
              onChange={(e) => setWeight(e.target.value)}
              placeholder=" "
              required
            />
            <label htmlFor="weight">Weight(kg)</label>
          </div>
          <div className="input-container">
            <input
              type="number"
              id="age"
              value={age}
              onChange={(e) => setAge(e.target.value)}
              placeholder=" "
              required
            />
            <label htmlFor="age">Age</label>
          </div>
          <div className="select-container">
            <label htmlFor="activity_level">Activity Level</label>
            <select
              id="activity_level"
              value={activity_level}
              onChange={(e) => setActivityLevel(e.target.value)}
              required
            >
              <option value="Low">Low</option>
              <option value="Medium">Medium</option>
              <option value="High">High</option>
            </select>
          </div>
          {errorMessage && <div className="error-message">{errorMessage}</div>} {/* afisare mesaj de eroare */}
          <button type="submit">Continue</button>
        </form>
      </div>
    </div>
  );
};

export default SignupPage;