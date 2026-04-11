package com.routemesh.service;

import com.routemesh.dao.RideDAO;
import com.routemesh.model.Ride;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class RideService {
    private final RideDAO rideDAO;

    public RideService(RideDAO rideDAO) {
        this.rideDAO = rideDAO;
    }

    public int createRide(int driverId, String source, String destination, Date date, Time time, int seats, double price) {
        Ride ride = new Ride();
        ride.setDriverId(driverId);
        ride.setSource(source);
        ride.setDestination(destination);
        ride.setRideDate(date);
        ride.setRideTime(time);
        ride.setAvailableSeats(seats);
        ride.setPricePerSeat(price);
        ride.setStatus("AVAILABLE");
        return rideDAO.createRide(ride);
    }

    public List<Ride> getMyRides(int driverId) {
        return rideDAO.getRidesByDriver(driverId);
    }

    public void cancelRide(int rideId) {
        rideDAO.updateRideStatus(rideId, "CANCELLED");
    }

    public List<Ride> searchRides(String source, String destination, Date date) {
        return rideDAO.searchRides(source, destination, date);
    }

    public Ride getRideById(int rideId) {
        return rideDAO.getRideById(rideId);
    }
}
