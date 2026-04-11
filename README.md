# RouteMesh 🚗

RouteMesh is a modern, full-stack carpooling platform designed to connect drivers and passengers for smarter, greener travel. It features a stunning "Glassmorphism" UI, real-time ride searching, and a role-based dashboard for both drivers and passengers.

## 🚀 Quick Start

### 1. Prerequisites
- **Node.js**: Version 18+
- **PostgreSQL**: Running on `localhost:5432`
- **Database Name**: `ride_db` (Password: `YOUR_PASSWORD`)

### 2. Backend Setup (Node.js)
The backend handles authentication, ride management, and seat availability.
```bash
cd NodeBackend
npm install
node index.js
```
*Server runs on: http://localhost:8080*

### 3. Frontend Setup (React + Vite)
The frontend provides a premium, interactive user experience.
```bash
cd Frontend
npm install
npm run dev
```
*App runs on: http://localhost:5173*

---

## 🔑 Login Credentials

Since the system uses secure **BCrypt encryption**, we recommend creating a new account via the **Sign Up** page. However, you can use these pre-registered demo credentials:

| Role | Email | Password |
| :--- | :--- | :--- |
| **Demo User** | `test_final@example.com` | `password123` |
| **Demo Driver** | `driver@example.com` | `password123` |

*Note: You can also register a new account instantly to choose your own role (Driver or Passenger).*

---

## ✨ Features

### 👤 For Passengers
- **Smart Search**: Find rides by source, destination, and date.
- **Easy Booking**: One-click booking with instant seat availability updates.
- **Savings Tracker**: View your total bookings and estimated savings.

### 🚘 For Drivers
- **Offer Rides**: Publish new trips via a simple modal form.
- **Earnings Dashboard**: Track your total rides and estimated earnings.
- **Rating System**: View your performance and user feedback.

## 🛠 Tech Stack
- **Frontend**: React, Vite, Framer Motion (Animations), Lucide React (Icons), Axios.
- **Backend**: Node.js, Express, PG (PostgreSQL client), Bcrypt.js (Security), CORS.
- **Database**: PostgreSQL with Transactional Safety on bookings.

---
*Created with ❤️ by Ritik Raj.*
