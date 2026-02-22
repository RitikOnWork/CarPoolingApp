-- Create Database
-- CREATE DATABASE car_pooling;

-- Users Table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('DRIVER', 'PASSENGER')),
    rating DECIMAL(3, 2) DEFAULT 5.0
);

-- Rides Table
CREATE TABLE rides (
    id SERIAL PRIMARY KEY,
    driver_id INT REFERENCES users(id),
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    ride_date DATE NOT NULL,
    ride_time TIME NOT NULL,
    available_seats INT NOT NULL,
    price_per_seat DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE'
);

-- Bookings Table
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    ride_id INT REFERENCES rides(id),
    passenger_id INT REFERENCES users(id),
    seats_booked INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    booking_status VARCHAR(20) DEFAULT 'CONFIRMED'
);
