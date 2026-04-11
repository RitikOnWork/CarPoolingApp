package com.routemesh.service;

import com.routemesh.dao.BookingDAO;
import com.routemesh.dao.RideDAO;
import com.routemesh.model.Booking;
import com.routemesh.model.Ride;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {
    private final BookingDAO bookingDAO;
    private final RideDAO rideDAO;

    public BookingService(BookingDAO bookingDAO, RideDAO rideDAO) {
        this.bookingDAO = bookingDAO;
        this.rideDAO = rideDAO;
    }

    @Transactional
    public boolean bookRide(int rideId, int passengerId, int seats) {
        Ride ride = rideDAO.getRideById(rideId); // Ideal for update lock if using JPA, here simplified for JDBC
        if (ride == null || !"AVAILABLE".equals(ride.getStatus()) || ride.getAvailableSeats() < seats) {
            return false;
        }

        Booking booking = new Booking();
        booking.setRideId(rideId);
        booking.setPassengerId(passengerId);
        booking.setSeatsBooked(seats);
        booking.setTotalPrice(seats * ride.getPricePerSeat());
        booking.setBookingStatus("CONFIRMED");
        
        bookingDAO.createBooking(booking);
        rideDAO.updateAvailableSeats(rideId, -seats);
        
        return true;
    }

    public List<Booking> getMyBookings(int passengerId) {
        return bookingDAO.getBookingsByPassenger(passengerId);
    }

    public List<Booking> getRideBookings(int rideId) {
        return bookingDAO.getBookingsByRide(rideId);
    }

    @Transactional
    public void cancelBooking(int bookingId) {
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking != null && !"CANCELLED".equals(booking.getBookingStatus())) {
            bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
            rideDAO.updateAvailableSeats(booking.getRideId(), booking.getSeatsBooked());
        }
    }
}
