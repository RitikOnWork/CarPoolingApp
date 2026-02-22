package service;

import dao.BookingDAO;
import dao.RideDAO;
import model.Booking;
import model.Ride;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookingService {
    private BookingDAO bookingDAO = new BookingDAO();
    private RideDAO rideDAO = new RideDAO();

    public boolean bookRide(int rideId, int passengerId, int seats) throws SQLException {
        Connection conn = DatabaseUtil.getConnection();
        try {
            conn.setAutoCommit(false);

            // 1. Get ride with lock for update
            Ride ride = rideDAO.getRideByIdForUpdate(conn, rideId);
            if (ride == null || !"AVAILABLE".equals(ride.getStatus())) {
                throw new SQLException("Ride not available.");
            }

            // 2. Check seats
            if (ride.getAvailableSeats() < seats) {
                throw new SQLException("Not enough seats available.");
            }

            // 3. Create booking
            Booking booking = new Booking();
            booking.setRideId(rideId);
            booking.setPassengerId(passengerId);
            booking.setSeatsBooked(seats);
            booking.setTotalPrice(seats * ride.getPricePerSeat());
            booking.setBookingStatus("CONFIRMED");
            bookingDAO.createBooking(conn, booking);

            // 4. Update ride seats
            rideDAO.updateAvailableSeats(conn, rideId, -seats);

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public List<Booking> getMyBookings(int passengerId) throws SQLException {
        return bookingDAO.getBookingsByPassenger(passengerId);
    }

    public List<Booking> getRideBookings(int rideId) throws SQLException {
        return bookingDAO.getBookingsByRide(rideId);
    }

    public void cancelBooking(int bookingId) throws SQLException {
        Connection conn = DatabaseUtil.getConnection();
        try {
            conn.setAutoCommit(false);

            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null || "CANCELLED".equals(booking.getBookingStatus())) {
                throw new SQLException("Booking not found or already cancelled.");
            }

            // Update booking status
            bookingDAO.updateBookingStatus(conn, bookingId, "CANCELLED");

            // Restore seats
            rideDAO.updateAvailableSeats(conn, booking.getRideId(), booking.getSeatsBooked());

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
}
