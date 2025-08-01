import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route} from "react-router-dom";
import HomePage from "./components/JS/HomePage";
import LoginPage from "./components/JS/LoginPage";
import SignupPage from "./components/JS/SignupPage";
import UserPage from "./components/JS/UserPage";
import AdminPage from "./components/JS/AdminPage";
import CalisthenicPage from "./components/JS/CalisthenicPage";
import DishPage from "./components/JS/DishPage";
import PlanPage from "./components/JS/PlanPage";
import { jwtDecode as jwt_decode } from "jwt-decode";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      const decodedToken = jwt_decode(token);
      const validRoles = ["user", "admin"];
      const userRoleValue = validRoles.includes(String(decodedToken.role))
        ? String(decodedToken.role)
        : null;
      setUserRole(userRoleValue);
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogin = () => {
    setIsLoggedIn(true);
  };

  return (
    <Router>
      <div>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage onLogin={handleLogin} />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/user" element={<UserPage />} />
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/calisthenic" element={<CalisthenicPage />} />
          <Route path="/dish" element={<DishPage />} />
          <Route path="/fitnessplan" element={<PlanPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
