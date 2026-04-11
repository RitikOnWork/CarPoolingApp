import React, { useState } from 'react';
import axios from 'axios';
import { motion } from 'framer-motion';
import { MapPin, Calendar, Search, Car } from 'lucide-react';

const SearchRides = ({ user }) => {
  const [query, setQuery] = useState({ source: '', destination: '', date: '' });
  const [results, setResults] = useState([]);
  const [searching, setSearching] = useState(false);
  const [msg, setMsg] = useState({ text: '', type: '' });

  const handleSearch = async (e) => {
    e.preventDefault();
    setSearching(true);
    setMsg({ text: '', type: '' });
    try {
      const res = await axios.get(`http://localhost:8080/api/rides/search`, { params: query });
      setResults(res.data);
      if (res.data.length === 0) {
        setMsg({ text: 'No rides found for these criteria.', type: 'error' });
      }
    } catch (err) {
      console.error(err);
      setMsg({ text: 'Error searching for rides.', type: 'error' });
    } finally {
      setSearching(false);
    }
  };

  const handleBook = async (rideId) => {
    if (!user) {
      setMsg({ text: 'Please login to book a ride.', type: 'error' });
      return;
    }
    try {
      await axios.post('http://localhost:8080/api/bookings', {
        rideId: rideId,
        passengerId: user.id,
        seats: 1
      });
      setMsg({ text: 'Ride booked successfully!', type: 'success' });
      // Update local results count
      setResults(results.map(r => r.id === rideId ? {...r, availableSeats: r.availableSeats - 1} : r));
    } catch (err) {
      setMsg({ text: err.response?.data?.error || 'Booking failed.', type: 'error' });
    }
  };

  return (
    <div style={{ padding: '40px' }}>
      {msg.text && (
        <div style={{ 
          padding: '15px', 
          borderRadius: '12px', 
          marginBottom: '20px', 
          textAlign: 'center',
          background: msg.type === 'success' ? 'rgba(16,185,129,0.1)' : 'rgba(244,63,94,0.1)',
          color: msg.type === 'success' ? '#10b981' : '#f43f5e',
          border: `1px solid ${msg.type === 'success' ? '#10b981' : '#f43f5e'}`
        }}>
          {msg.text}
        </div>
      )}

      <div className="glass-card" style={{ padding: '30px', marginBottom: '40px' }}>
        <form onSubmit={handleSearch} style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '20px', alignItems: 'end' }}>
          <div className="input-group" style={{ marginBottom: 0 }}>
            <label><MapPin size={14} /> From</label>
            <input type="text" placeholder="Source city" required onChange={e => setQuery({...query, source: e.target.value})} />
          </div>
          <div className="input-group" style={{ marginBottom: 0 }}>
            <label><MapPin size={14} /> To</label>
            <input type="text" placeholder="Destination" required onChange={e => setQuery({...query, destination: e.target.value})} />
          </div>
          <div className="input-group" style={{ marginBottom: 0 }}>
            <label><Calendar size={14} /> Date</label>
            <input type="date" required onChange={e => setQuery({...query, date: e.target.value})} />
          </div>
          <button className="btn btn-primary" style={{ height: '48px', justifyContent: 'center' }} disabled={searching}>
            {searching ? 'Finding...' : 'Search Rides'}
          </button>
        </form>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '24px' }}>
        {results.map((ride, i) => (
          <motion.div 
            key={ride.id}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: i * 0.1 }}
            className="glass-card" 
            style={{ padding: '24px' }}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: '20px' }}>
              <div>
                <h4 style={{ color: 'var(--text-muted)', fontSize: '0.8rem', textTransform: 'uppercase' }}>Available Ride</h4>
                <div style={{ fontSize: '1.2rem', fontWeight: 'bold', marginTop: '5px' }}>
                  {ride.source} → {ride.destination}
                </div>
              </div>
              <div style={{ background: 'var(--glass)', padding: '8px 12px', borderRadius: '12px', color: 'var(--primary)', fontWeight: 'bold' }}>
                ${ride.pricePerSeat}
              </div>
            </div>

            <div style={{ display: 'flex', gap: '20px', fontSize: '0.9rem', color: 'var(--text-muted)', marginBottom: '20px' }}>
              <span style={{ display: 'flex', alignItems: 'center', gap: '5px' }}><Calendar size={16} /> {ride.rideDate}</span>
              <span style={{ display: 'flex', alignItems: 'center', gap: '5px' }}><Car size={16} /> {ride.availableSeats} seats</span>
            </div>

            <button 
              onClick={() => handleBook(ride.id)} 
              disabled={ride.availableSeats <= 0}
              className="btn btn-primary" 
              style={{ width: '100%', opacity: ride.availableSeats <= 0 ? 0.5 : 1 }}
            >
              {ride.availableSeats <= 0 ? 'Full' : 'Book Now'}
            </button>
          </motion.div>
        ))}

        {results.length === 0 && !searching && (
          <div style={{ gridColumn: '1/-1', textAlign: 'center', padding: '100px', color: 'var(--text-muted)' }}>
            <Search size={48} style={{ opacity: 0.2, marginBottom: '20px' }} />
            <p>Enter your travel details to find available rides.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default SearchRides;
