package com.routemesh.model;

import java.sql.Date;
import java.sql.Time;

public class Ride {
    private int id;
    private int driverId;
    private String source;
    private String destination;
    private Date rideDate;
    private Time rideTime;
    private int availableSeats;
    private double pricePerSeat;
    private String status; // e.g., AVAILABLE, CANCELLED, COMPLETED

    public Ride() {}

    public Ride(int id, int driverId, String source, String destination, Date rideDate, Time rideTime, int availableSeats, double pricePerSeat, String status) {
        this.id = id;
        this.driverId = driverId;
        this.source = source;
        this.destination = destination;
        this.rideDate = rideDate;
        this.rideTime = rideTime;
        this.availableSeats = availableSeats;
        this.pricePerSeat = pricePerSeat;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Date getRideDate() { return rideDate; }
    public void setRideDate(Date rideDate) { this.rideDate = rideDate; }

    public Time getRideTime() { return rideTime; }
    public void setRideTime(Time rideTime) { this.rideTime = rideTime; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public double getPricePerSeat() { return pricePerSeat; }
    public void setPricePerSeat(double pricePerSeat) { this.pricePerSeat = pricePerSeat; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
