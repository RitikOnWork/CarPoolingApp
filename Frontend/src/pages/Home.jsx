import React from 'react';
import { motion } from 'framer-motion';
import { ArrowRight, Shield, Clock, Zap } from 'lucide-react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div style={{ padding: '80px 40px' }}>
      <header style={{ textAlign: 'center', maxWidth: '800px', margin: '0 auto' }}>
        <motion.span
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          style={{ background: 'var(--glass)', padding: '8px 16px', borderRadius: '100px', fontSize: '0.8rem', color: 'var(--primary)', border: '1px solid var(--glass-border)' }}
        >
          Connecting Commuters with Comfort
        </motion.span>
        
        <motion.h1
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          style={{ fontSize: '4rem', fontWeight: 'bold', marginTop: '24px', lineHeight: '1.2' }}
        >
          Travel Smarter, <br />
          <span style={{ background: 'linear-gradient(90deg, #6366f1, #f43f5e)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>Share Your Journey</span>
        </motion.h1>

        <motion.p
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          style={{ color: 'var(--text-muted)', fontSize: '1.2rem', marginTop: '24px' }}
        >
          RouteMesh is the next-gen car-pooling platform. Save money, reduce carbon footprint, and meet amazing people on every ride.
        </motion.p>

        <motion.div
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ delay: 0.3 }}
          style={{ marginTop: '40px', display: 'flex', gap: '20px', justifyContent: 'center' }}
        >
          <Link to="/search" className="btn btn-primary" style={{ padding: '16px 32px', fontSize: '1.1rem' }}>
            Find a Ride <ArrowRight />
          </Link>
          <Link to="/auth" className="btn btn-outline" style={{ padding: '16px 32px', fontSize: '1.1rem' }}>
            Start Driving
          </Link>
        </motion.div>
      </header>

      <section style={{ marginTop: '120px', display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '30px' }}>
        {[
          { icon: <Shield color="#10b981" />, title: "Secure Rides", desc: "Verified drivers and real-time tracking for your safety." },
          { icon: <Clock color="#f59e0b" />, title: "Time Efficient", desc: "Optimized routes to get you to your destination faster." },
          { icon: <Zap color="#6366f1" />, title: "Hyper Local", desc: "Find riders and drivers in your immediate vicinity." }
        ].map((feature, i) => (
          <motion.div
            key={i}
            whileHover={{ y: -10 }}
            className="glass-card"
            style={{ padding: '40px', textAlign: 'center' }}
          >
            <div style={{ background: 'rgba(255,255,255,0.05)', width: '60px', height: '60px', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 20px' }}>
              {feature.icon}
            </div>
            <h3 style={{ marginBottom: '12px' }}>{feature.title}</h3>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>{feature.desc}</p>
          </motion.div>
        ))}
      </section>
    </div>
  );
};

export default Home;
