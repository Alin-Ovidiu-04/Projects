
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Link, Routes, useNavigate } from "react-router-dom";
import './style.css';

const App = () => {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="/patients" element={<Patients />} />
          <Route path="/doctors" element={<Doctors />} />
          <Route path="/patients/personalinfo" element={<PatientsPersonalInfo />} />
          <Route path="/patients/medicalhistory" element={<PatientsMedicalHistory />} />
          <Route path="/patients/doctorslist" element={<PatientsDoctorsList />} />
          <Route path="/patients/appointments" element={<PatientsAppointments />} />
          <Route path="/patients/newappointment" element={<PatientsNewAppointment />} />
          <Route path="/doctors/personalinfo" element={<DoctorsPersonalInfo />} />
          <Route path="/doctors/patientslist" element={<DoctorsPatientsList />} />
          <Route path="/doctors/appointments" element={<DoctorsAppointments />} />
          <Route path="/doctors/consultations" element={<DoctorsConsultations />} />
        
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
      <Link to="/patients">
        <button>Patients</button>
      </Link>
      <Link to="/doctors">
        <button>Doctors</button>
      </Link>
    </div>
  );
};


const SignIn = () => {
  const [nume, setNume] = useState('');
  const [prenume, setPrenume] = useState('');
  const [email, setEmail] = useState('');
  const [parola, setParola] = useState('');
  const [submitClicked, setSubmitClicked] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (submitClicked) {
      const fetchData = async () => {
        try {
          const data = { nume, prenume, email, parola };
          const response = await fetch('http://localhost:8081/register', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
          });

          if (response.ok) {
            console.log('Autentificare reușită!', response);
            // Redirect către pagina "home" dacă răspunsul de la server este OK
            navigate('/'); // Ajustează ruta potrivit nevoilor tale
          } else {
            console.log('Autentificare eșuată!', response);
            // Poți gestiona altfel cazurile de eroare sau afișa un mesaj utilizatorului
          }
        } catch (error) {
          console.error('Eroare în timpul cererii către server:', error);
        }
      };

      fetchData();
    }
  }, [submitClicked, nume, prenume, email, parola, navigate]);

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Form submitted');
    setSubmitClicked(true);
  };

  return (
    <div>
      <h2>Înregistrare</h2>
      <form onSubmit={handleSubmit}>
        {/* Inputurile pentru datele de autentificare */}
        <input
          type="text"
          name="nume"
          placeholder="Nume"
          value={nume}
          onChange={(e) => setNume(e.target.value)}
        />
        <input
          type="text"
          name="prenume"
          placeholder="Prenume"
          value={prenume}
          onChange={(e) => setPrenume(e.target.value)}
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          name="parola"
          placeholder="Parola"
          value={parola}
          onChange={(e) => setParola(e.target.value)}
        />
        <button type="submit">Submit</button>
      </form>
    </div>
  );
};

const Login = () => {
  const [email, setEmail] = useState('');
  const [parola, setParola] = useState('');
  const [submitClicked, setSubmitClicked] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (submitClicked) {
      const fetchData = async () => {
        try {
          const data = { email, parola };
          const response = await fetch('http://localhost:8081/authenticate', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
          });

          if (response.ok) {
            console.log('Înregistrare reușită!', response);
            // Redirect către pagina "home" dacă răspunsul de la server este OK
            navigate('/');
          } else {
            console.log('Înregistrare eșuată!', response);
            // Poți gestiona altfel cazurile de eroare sau afișa un mesaj utilizatorului
          }
        } catch (error) {
          console.error('Eroare în timpul cererii către server:', error);
        }
      };

      fetchData();
    }
  }, [submitClicked, email, parola, navigate]);

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Formularul a fost trimis');
    setSubmitClicked(true);
  };

  return (
    <div>
      <h2>LogIn</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          name="parola"
          placeholder="Parola"
          value={parola}
          onChange={(e) => setParola(e.target.value)}
        />
        <button type="submit">Înregistrează-te</button>
      </form>
    </div>
  );
};

const Patients = () => {
  return (
    <div>
      <h2>Patients</h2>
      <Link to="/patients/personalinfo">
        <button>Personal Info</button>
      </Link>
      <Link to="/patients/medicalhistory">
        <button>Medical History</button>
      </Link>
      <Link to="/patients/doctorslist">
        <button>Doctors List</button>
      </Link>
      <Link to="/patients/appointments">
        <button>Appointments</button>
      </Link>
      <Link to="/patients/newappointment">
        <button>New Appointment</button>
      </Link>
    </div>
  );
};

const Doctors = () => {
  return (
    <div>
      <h2>Doctors</h2>
      <Link to="/doctors/personalinfo">
        <button>Personal Info</button>
      </Link>
      <Link to="/doctors/patientslist">
        <button>Patients List</button>
      </Link>
      <Link to="/doctors/appointments">
        <button>Appointments</button>
      </Link>
      <Link to="/doctors/consultations">
        <button>Consultations</button>
      </Link>
    </div>
  );
};

const PatientsPersonalInfo = () => {

  const navigate = useNavigate();

  return (
    <div>
      <h2>Personal Info</h2>
      <button onClick={() => navigate(-1)}>Back</button>
    </div>
  );
};

const PatientsMedicalHistory = () => {

  const navigate = useNavigate();

  return (
    <div>
      <h2>Medical History</h2>
      <button onClick={() => navigate(-1)}>Back</button>
    </div>
  );
};

const PatientsDoctorsList = () => {

  const navigate = useNavigate();

  return (
    <div>
      <h2>Doctors List</h2>
      <button onClick={() => navigate(-1)}>Back</button>
    </div>
  );
};

const PatientsAppointments = () => {

  const navigate = useNavigate();

  return (
    <div>
      <h2>Appointments</h2>
      <button onClick={() => navigate(-1)}>Back</button>
    </div>
  );
};

const PatientsNewAppointment = () => {

  const navigate = useNavigate();

  return (
    <div>
      <h2>New Appointment</h2>
      <button onClick={() => navigate(-1)}>Back</button>
    </div>
  );
};

const DoctorsPersonalInfo = () => {

  const navigate = useNavigate();

  return (
    <div>
      <h2>Personal Info</h2>
      <button onClick={() => navigate(-1)}>Back</button>
    </div>
  );
};

const DoctorsPatientsList = () => {

  const navigate = useNavigate();

  return (
    <div>
      <h2>Patients List</h2>
      <button onClick={() => navigate(-1)}>Back</button>
    </div>
  );
};

const DoctorsAppointments = () => {

  const navigate = useNavigate();

  return (
    <div>
      <h2>Appointments</h2>
      <button onClick={() => navigate(-1)}>Back</button>
    </div>
  );
};

const DoctorsConsultations = () => {
  const navigate = useNavigate();
  const [consultations, setConsultations] = useState([]);

  useEffect(() => {
    // Definirea funcției fetchData pentru a obține consultațiile
    const fetchData = async () => {
      try {
        const response = await fetch('http://localhost:8081/api/medical_office/consultations');
        if (response.ok) {
          const data = await response.json();
          setConsultations(data);
        } else {
          console.log('Fetch failed', response);
        }
      } catch (error) {
        console.error('Error during fetch:', error);
      }
    };

    // Apelarea funcției fetchData la încărcarea componentei
    fetchData();
  }, []); // Aici este important să pui un array gol pentru a asigura că aceasta rulează doar o dată la încărcare

  return (
    <div>
      <h2>Consultations</h2>
  
      {/* Afișarea listei de consultații */}
      <ul>
        {consultations.map((consultation, index) => (
          <li key={index}> Id pacient: {consultation.id_pacient} - Id doctor: {consultation.id_doctor} - Data: {consultation.date}
          <div>Diagnostic: {consultation.diagnostic}</div>
          <div>Investigatii: {consultation.investigations.map((investigation, indx) => (
            <div key={indx}>
              - Denumire: {investigation.name}, Durata: {investigation.time},Rezultat: {investigation.result}
            </div>
          ))}
          </div>
          </li>
        ))}
      </ul>

      <button onClick={() => navigate(-1)}>Back</button>
    </div>
  );
};

export default App;