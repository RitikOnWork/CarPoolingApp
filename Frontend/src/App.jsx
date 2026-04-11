import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Auth from './pages/Auth';
import Dashboard from './pages/Dashboard';
import SearchRides from './pages/SearchRides';

function App() {
  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  useEffect(() => {
    localStorage.setItem('user', JSON.stringify(user));
  }, [user]);

  return (
    <Router>
      <div className="app">
        <Navbar user={user} setUser={setUser} />
        <main className="container">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/auth" element={<Auth setUser={setUser} />} />
            <Route path="/search" element={<SearchRides user={user} />} />
            <Route 
              path="/dashboard" 
              element={user ? <Dashboard user={user} /> : <Navigate to="/auth" />} 
            />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
