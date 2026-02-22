package service;

import dao.RideDAO;
import model.Ride;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class RideService {
    private RideDAO rideDAO = new RideDAO();

    public int createRide(int driverId, String source, String destination, Date date, Time time, int seats,
            double price) throws SQLException {
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

    public List<Ride> getMyRides(int driverId) throws SQLException {
        return rideDAO.getRidesByDriver(driverId);
    }

    public void cancelRide(int rideId) throws SQLException {
        rideDAO.updateRideStatus(rideId, "CANCELLED");
    }

    public List<Ride> searchRides(String source, String destination, Date date) throws SQLException {
        return rideDAO.searchRides(source, destination, date);
    }

    public Ride getRideById(int rideId) throws SQLException {
        return rideDAO.getRideById(rideId);
    }
}
