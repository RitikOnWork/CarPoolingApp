-- Create Database
-- CREATE DATABASE car_pooling;

-- Users Table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('DRIVER', 'PASSENGER')),
    rating DECIMAL(3, 2) DEFAULT 5.0,
    total_ratings INT DEFAULT 0
);

-- Rides Table
CREATE TABLE rides (
    id SERIAL PRIMARY KEY,
    driver_id INT REFERENCES users(id) ON DELETE CASCADE,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    ride_date DATE NOT NULL,
    ride_time TIME NOT NULL,
    available_seats INT NOT NULL,
    price_per_seat DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE' -- AVAILABLE, CANCELLED, COMPLETED
);

-- Bookings Table
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    ride_id INT REFERENCES rides(id) ON DELETE CASCADE,
    passenger_id INT REFERENCES users(id) ON DELETE CASCADE,
    seats_booked INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    booking_status VARCHAR(20) DEFAULT 'CONFIRMED' -- CONFIRMED, CANCELLED
);

