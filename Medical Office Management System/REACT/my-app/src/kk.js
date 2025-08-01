import React from 'react';
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Link, Routes, useNavigate } from "react-router-dom";


const App = () => {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signin" element={<SignIn />} />
        </Routes>
      </div>
    </Router>
  );
};

const Home = () => {
  return (
    <div>
      <Link to="/login">
        <button>Log in</button>
      </Link>
      <Link to="/signin">
        <button>Sign in</button>
      </Link>
    </div>
  );
};


const Login = () => {
  const handleSubmit = async (e) => {
    e.preventDefault();

    console.log('Form submitted'); // Verificare în consolă pentru a vedea dacă funcția este apelată

    const formData = new FormData(e.target);
    const data = {
      nume: formData.get('nume'),
      prenume: formData.get('prenume'),
      email: formData.get('email'),
      parola: formData.get('parola'),
    };

    try {
      const response = await fetch('http://localhost:8080/authenticate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      });

      if (response.ok) {
        console.log('Autentificare reușită!', response); // Verificare pentru un răspuns OK de la server
      } else {
        console.log('Autentificare eșuată!', response); // Verificare pentru un posibil mesaj de eroare de la server
      }
    } catch (error) {
      console.error('Eroare în timpul cererii către server:', error);
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        {/* Inputurile pentru datele de autentificare */}
        <input type="text" name="nume" placeholder="Nume" />
        <input type="text" name="prenume" placeholder="Prenume" />
        <input type="email" name="email" placeholder="Email" />
        <input type="password" name="parola" placeholder="Parola" />
        <button type="submit">Submit</button>
      </form>
    </div>
  );
};

const SignIn = () => {
  // Component pentru procesul de înregistrare (Sign in)
  return (
    <div>
      <h2>Sign in</h2>
      {/* Formular pentru înregistrare */}
    </div>
  );
};

export default App;