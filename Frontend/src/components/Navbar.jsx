import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Car, User, LogOut, Search } from 'lucide-react';
import { motion } from 'framer-motion';

const Navbar = ({ user, setUser }) => {
  const navigate = useNavigate();

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('user');
    navigate('/');
  };

  return (
    <nav className="navbar" style={{
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      padding: '20px 40px',
      position: 'sticky',
      top: 0,
      zIndex: 1000,
      background: 'rgba(15, 23, 42, 0.8)',
      backdropFilter: 'blur(10px)',
      borderBottom: '1px solid var(--glass-border)'
    }}>
      <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: '10px', textDecoration: 'none', color: 'white', fontSize: '1.5rem', fontWeight: 'bold' }}>
        <motion.div
          animate={{ rotate: [0, 10, -10, 0] }}
          transition={{ duration: 5, repeat: Infinity }}
        >
          <Car size={32} color="var(--primary)" />
        </motion.div>
        Route<span style={{ color: 'var(--primary)' }}>Mesh</span>
      </Link>

      <div style={{ display: 'flex', gap: '30px', alignItems: 'center' }}>
        <Link to="/search" style={{ color: 'var(--text-main)', textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '5px' }}>
          <Search size={18} /> Search
        </Link>
        {user ? (
          <>
            <Link to="/dashboard" style={{ color: 'var(--text-main)', textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '5px' }}>
              <User size={18} /> Dashboard
            </Link>
            <button onClick={handleLogout} className="btn btn-outline" style={{ padding: '8px 16px' }}>
              <LogOut size={18} /> Logout
            </button>
          </>
        ) : (
          <Link to="/auth" className="btn btn-primary">Join Now</Link>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
