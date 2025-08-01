import React from "react";
import { useNavigate } from "react-router-dom";
import "../CSS/HomePage.css";

const HomePage = () => {
    const navigate = useNavigate();

    return (
        <div className="container">
            <div className="content">
                <h1>Welcome to our website!</h1>
                <div className="button-container">
                    <button onClick={() => navigate("/login")} className="button">Log in</button>
                    <button onClick={() => navigate("/signup")} className="button">Sign up</button>
                </div>
            </div>
        </div>
    );
}

export default HomePage;