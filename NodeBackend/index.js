require('dotenv').config();
const express = require('express');
const { Pool } = require('pg');
const cors = require('cors');
const bcrypt = require('bcryptjs');

const app = express();
const port = process.env.PORT || 8080;

app.use(cors());
app.use(express.json());

const pool = new Pool({
  user: process.env.DB_USER || 'postgres',
  host: process.env.DB_HOST || 'localhost',
  database: process.env.DB_NAME || 'ride_db',
  password: process.env.DB_PASSWORD || 'ritik2006',
  port: process.env.DB_PORT || 5432,
});


// Auth Routes
app.post('/api/auth/register', async (req, res) => {
  const { name, email, password, role } = req.body;
  try {
    const hashedPassword = await bcrypt.hash(password, 10);
    const result = await pool.query(
      'INSERT INTO users (name, email, password, role, average_rating, total_ratings) VALUES ($1, $2, $3, $4, $5, $6) RETURNING id',
      [name, email, hashedPassword, role, 5.0, 0]
    );
    res.json({ id: result.rows[0].id, message: 'User registered successfully' });
  } catch (err) {
    if (err.code === '23505') {
       return res.status(400).json({ error: 'Email already registered. Try a different one.' });
    }
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/auth/login', async (req, res) => {
  const { email, password } = req.body;
  try {
    const result = await pool.query('SELECT * FROM users WHERE email = $1', [email]);
    if (result.rows.length === 0) return res.status(401).json({ error: 'Invalid credentials' });

    const user = result.rows[0];
    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) return res.status(401).json({ error: 'Invalid credentials' });

    res.json({
      id: user.id,
      name: user.name,
      role: user.role,
      rating: user.average_rating
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/auth/health', (req, res) => res.send('Backend is UP and Running!'));

// Ride Routes
app.post('/api/rides', async (req, res) => {
  const { driverId, source, destination, date, time, seats, price } = req.body;
  const departureTime = `${date} ${time}`;
  try {
    const result = await pool.query(
      'INSERT INTO rides (driver_id, source, destination, departure_time, seats, fare, status) VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING id',
      [driverId, source, destination, departureTime, seats, price, 'AVAILABLE']
    );
    res.json({ id: result.rows[0].id, message: 'Ride created successfully' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/rides/search', async (req, res) => {
  const { source, destination, date } = req.query;
  try {
    // Search by date (ignoring time)
    const result = await pool.query(
      'SELECT * FROM rides WHERE source = $1 AND destination = $2 AND departure_time::date = $3 AND seats > 0 AND status = $4',
      [source, destination, date, 'AVAILABLE']
    );
    
    const rides = result.rows.map(r => ({
        id: r.id,
        driverId: r.driver_id,
        source: r.source,
        destination: r.destination,
        rideDate: r.departure_time.toISOString().split('T')[0],
        rideTime: r.departure_time.toTimeString().split(' ')[0],
        availableSeats: r.seats,
        pricePerSeat: r.fare,
        status: r.status
    }));
    res.json(rides);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/rides/driver/:driverId', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM rides WHERE driver_id = $1', [req.params.driverId]);
    const rides = result.rows.map(r => ({
        ...r,
        ride_date: r.departure_time ? r.departure_time.toISOString().split('T')[0] : 'N/A',
        ride_time: r.departure_time ? r.departure_time.toTimeString().split(' ')[0] : 'N/A',
        price_per_seat: r.fare
    }));
    res.json(rides);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Booking Routes
app.post('/api/bookings', async (req, res) => {
  const { rideId, passengerId, seats } = req.body;
  const client = await pool.connect();
  try {
    await client.query('BEGIN');
    const rideRes = await client.query('SELECT * FROM rides WHERE id = $1 FOR UPDATE', [rideId]);
    const ride = rideRes.rows[0];

    if (!ride || ride.status !== 'AVAILABLE' || ride.seats < seats) {
      throw new Error('Ride not available or not enough seats');
    }

    const totalPrice = seats * ride.fare;
    const bookingRes = await client.query(
      'INSERT INTO bookings (ride_id, passenger_id, seats_booked, total_price, booking_status) VALUES ($1, $2, $3, $4, $5) RETURNING id',
      [rideId, passengerId, seats, totalPrice, 'CONFIRMED']
    );

    await client.query('UPDATE rides SET seats = seats - $1 WHERE id = $2', [seats, rideId]);
    await client.query('COMMIT');

    res.json({ id: bookingRes.rows[0].id, message: 'Booking successful' });
  } catch (err) {
    await client.query('ROLLBACK');
    res.status(400).json({ error: err.message });
  } finally {
    client.release();
  }
});

app.get('/api/bookings/passenger/:passengerId', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM bookings WHERE passenger_id = $1', [req.params.passengerId]);
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.listen(port, () => console.log(`RouteMesh Node Backend running on port ${port}`));
