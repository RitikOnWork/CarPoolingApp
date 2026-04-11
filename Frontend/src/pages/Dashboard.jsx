import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Car, Clock, CheckCircle, MapPin, DollarSign, Plus, Search, Users, Trash2 } from 'lucide-react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const Dashboard = ({ user }) => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newRide, setNewRide] = useState({ source: '', destination: '', date: '', time: '', seats: 4, price: 10 });

  const fetchData = async () => {
    setLoading(true);
    try {
      const endpoint = user.role === 'DRIVER' 
        ? `/api/rides/driver/${user.id}` 
        : `/api/bookings/passenger/${user.id}`;
      const res = await axios.get(`http://localhost:8080${res.config?.url ? res.config.url : endpoint}`); 
      // Note: Re-fetching the correct URL if needed, but endpoint is solid.
      setData(res.data);
    } catch (err) {
      console.error("Failed to fetch dashboard data", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [user]);

  const handleCreateRide = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/api/rides', {
        ...newRide,
        driverId: user.id
      });
      setShowCreateModal(false);
      fetchData();
    } catch (err) {
      alert("Failed to create ride: " + err.message);
    }
  };

  const isDriver = user.role === 'DRIVER';

  return (
    <div style={{ padding: '40px' }}>
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '40px' }}>
        <div>
          <h1 style={{ fontSize: '2.5rem' }}>
            Hello, <span style={{ color: 'var(--primary)' }}>{user.name}</span>
          </h1>
          <p style={{ color: 'var(--text-muted)' }}>
            Manage your {isDriver ? 'offered rides and earnings' : 'bookings and upcoming trips'}.
          </p>
        </div>
        {isDriver ? (
          <button onClick={() => setShowCreateModal(true)} className="btn btn-primary">
            <Plus size={20} /> Offer a Ride
          </button>
        ) : (
          <Link to="/search" className="btn btn-primary">
            <Search size={20} /> Find a Ride
          </Link>
        )}
      </header>

      {/* Stats Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '20px', marginBottom: '40px' }}>
        {[
          { 
            label: isDriver ? "Active Rides" : "Total Bookings", 
            value: data.length, 
            icon: <Car color="var(--primary)" /> 
          },
          { 
            label: isDriver ? "Total Earnings" : "Money Saved", 
            value: `$${isDriver ? (data.length * 45).toFixed(0) : (data.length * 20).toFixed(0)}`, 
            icon: <DollarSign color="#10b981" /> 
          },
          { 
            label: "Member Rank", 
            value: user.rating >= 4.5 ? "Elite" : "Standard", 
            icon: <CheckCircle color="#6366f1" /> 
          },
          { 
            label: "User Rating", 
            value: user.rating.toFixed(1), 
            icon: <Users color="#f43f5e" /> 
          }
        ].map((stat, i) => (
          <motion.div 
            key={i} 
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: i * 0.1 }}
            className="glass-card" 
            style={{ padding: '24px', display: 'flex', alignItems: 'center', gap: '20px' }}
          >
            <div style={{ background: 'rgba(255,255,255,0.05)', padding: '12px', borderRadius: '12px' }}>{stat.icon}</div>
            <div>
              <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{stat.label}</div>
              <div style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>{stat.value}</div>
            </div>
          </motion.div>
        ))}
      </div>

      {/* Activity Section */}
      <section className="glass-card" style={{ padding: '30px', minHeight: '400px' }}>
        <h2 style={{ marginBottom: '24px', display: 'flex', alignItems: 'center', gap: '10px' }}>
          <Clock size={24} color="var(--primary)" /> {isDriver ? 'My Published Rides' : 'My Recent Bookings'}
        </h2>
        
        {loading ? (
          <div style={{ textAlign: 'center', padding: '100px' }}>Loading...</div>
        ) : data.length > 0 ? (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {data.map((item, i) => (
              <motion.div 
                key={i} 
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: i * 0.05 }}
                style={{ 
                  display: 'flex', 
                  justifyContent: 'space-between', 
                  alignItems: 'center', 
                  padding: '24px', 
                  borderRadius: '20px', 
                  background: 'rgba(255,255,255,0.02)', 
                  border: '1px solid var(--glass-border)' 
                }}
              >
                <div style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
                   <div style={{ width: '50px', height: '50px', background: 'var(--glass)', borderRadius: '14px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                      <MapPin size={24} color="var(--primary)" />
                   </div>
                   <div>
                      <div style={{ fontSize: '1.2rem', fontWeight: '600' }}>
                        {isDriver ? `${item.source} ➔ ${item.destination}` : `Booking for Ride #${item.ride_id}`}
                      </div>
                      <div style={{ fontSize: '0.9rem', color: 'var(--text-muted)', marginTop: '4px' }}>
                        {isDriver ? `${item.ride_date} at ${item.ride_time}` : `Status: ${item.booking_status}`}
                      </div>
                   </div>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <div style={{ fontWeight: 'bold', fontSize: '1.1rem', color: (item.status === 'CANCELLED' || item.booking_status === 'CANCELLED') ? '#ef4444' : '#10b981' }}>
                    {item.status || item.booking_status}
                  </div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginTop: '4px' }}>
                    {item.price_per_seat ? `$${item.price_per_seat} / seat` : `$${item.total_price} total`}
                  </div>
                </div>
              </motion.div>
            ))}
          </div>
        ) : (
          <div style={{ textAlign: 'center', padding: '80px', color: 'var(--text-muted)' }}>
            <div style={{ opacity: 0.2, marginBottom: '20px' }}>
              {isDriver ? <Car size={64} style={{margin:'0 auto'}} /> : <Search size={64} style={{margin:'0 auto'}} />}
            </div>
            <p style={{ fontSize: '1.1rem' }}>
              {isDriver ? "You haven't offered any rides yet." : "You haven't booked any rides yet."}
            </p>
            <button 
              onClick={() => isDriver ? setShowCreateModal(true) : navigate('/search')}
              className="btn btn-outline" 
              style={{ marginTop: '20px' }}
            >
              {isDriver ? "Create Your First Ride" : "Find Your First Ride"}
            </button>
          </div>
        )}
      </section>

      {/* Create Ride Modal */}
      <AnimatePresence>
        {showCreateModal && (
          <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.8)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 2000 }}>
            <motion.div 
              initial={{ opacity: 0, scale: 0.9, y: 20 }}
              animate={{ opacity: 1, scale: 1, y: 0 }}
              exit={{ opacity: 0, scale: 0.9, y: 20 }}
              className="glass-card" 
              style={{ width: '500px', padding: '40px' }}
            >
              <h2 style={{ marginBottom: '30px' }}>Offer a New Ride</h2>
              <form onSubmit={handleCreateRide}>
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
                  <div className="input-group">
                    <label>Source</label>
                    <input type="text" required onChange={e => setNewRide({...newRide, source: e.target.value})} />
                  </div>
                  <div className="input-group">
                    <label>Destination</label>
                    <input type="text" required onChange={e => setNewRide({...newRide, destination: e.target.value})} />
                  </div>
                </div>
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
                  <div className="input-group">
                    <label>Date</label>
                    <input type="date" required onChange={e => setNewRide({...newRide, date: e.target.value})} />
                  </div>
                  <div className="input-group">
                    <label>Time</label>
                    <input type="time" required onChange={e => setNewRide({...newRide, time: e.target.value + ":00"})} />
                  </div>
                </div>
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
                  <div className="input-group">
                    <label>Seats Available</label>
                    <input type="number" required min="1" onChange={e => setNewRide({...newRide, seats: parseInt(e.target.value)})} />
                  </div>
                  <div className="input-group">
                    <label>Price per Seat ($)</label>
                    <input type="number" required min="1" onChange={e => setNewRide({...newRide, price: parseFloat(e.target.value)})} />
                  </div>
                </div>
                <div style={{ display: 'flex', gap: '15px', marginTop: '20px' }}>
                  <button type="submit" className="btn btn-primary" style={{ flex: 1 }}>Create Ride</button>
                  <button type="button" onClick={() => setShowCreateModal(false)} className="btn btn-outline" style={{ flex: 1 }}>Cancel</button>
                </div>
              </form>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default Dashboard;
