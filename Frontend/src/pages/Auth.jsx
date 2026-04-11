import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Auth = ({ setUser }) => {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({ name: '', email: '', password: '', role: 'PASSENGER' });
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMsg('');
    try {
      const endpoint = isLogin ? '/api/auth/login' : '/api/auth/register';
      const res = await axios.post(`http://localhost:8080${endpoint}`, formData);
      
      if (isLogin) {
        setUser(res.data);
        navigate('/dashboard');
      } else {
        setMsg('Sign up successful! Please login.');
        setIsLogin(true);
      }
    } catch (err) {
      setMsg(err.response?.data?.error || 'Authentication failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh' }}>
      <motion.div 
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        className="glass-card" 
        style={{ width: '400px', padding: '40px' }}
      >
        <h2 style={{ textAlign: 'center', marginBottom: '30px' }}>
          {isLogin ? 'Welcome Back' : 'Create Account'}
        </h2>

        {msg && <div style={{ background: 'rgba(244,63,94,0.1)', color: '#f43f5e', padding: '10px', borderRadius: '8px', marginBottom: '20px', fontSize: '0.8rem', textAlign: 'center' }}>{msg}</div>}

        <form onSubmit={handleSubmit}>
          {!isLogin && (
            <div className="input-group">
              <label>Full Name</label>
              <input 
                type="text" 
                required 
                onChange={e => setFormData({...formData, name: e.target.value})} 
                placeholder="John Doe"
              />
            </div>
          )}
          <div className="input-group">
            <label>Email Address</label>
            <input 
              type="email" 
              required 
              onChange={e => setFormData({...formData, email: e.target.value})} 
              placeholder="john@example.com"
            />
          </div>
          <div className="input-group">
            <label>Password</label>
            <input 
              type="password" 
              required 
              onChange={e => setFormData({...formData, password: e.target.value})} 
              placeholder="••••••••"
            />
          </div>
          {!isLogin && (
            <div className="input-group">
              <label>I want to be a</label>
              <select onChange={e => setFormData({...formData, role: e.target.value})}>
                <option value="PASSENGER">Passenger</option>
                <option value="DRIVER">Driver</option>
              </select>
            </div>
          )}

          <button className="btn btn-primary" style={{ width: '100%', marginTop: '10px' }} disabled={loading}>
            {loading ? 'Processing...' : (isLogin ? 'Sign In' : 'Sign Up')}
          </button>
        </form>

        <p style={{ textAlign: 'center', marginTop: '20px', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
          {isLogin ? "Don't have an account?" : "Already have an account?"}
          <button 
            onClick={() => setIsLogin(!isLogin)} 
            style={{ background: 'none', border: 'none', color: 'var(--primary)', fontWeight: 'bold', marginLeft: '5px', cursor: 'pointer' }}
          >
            {isLogin ? 'Sign Up' : 'Sign In'}
          </button>
        </p>
      </motion.div>
    </div>
  );
};

export default Auth;
