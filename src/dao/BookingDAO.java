package dao;

import model.Booking;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    public int createBooking(Connection conn, Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (ride_id, passenger_id, seats_booked, total_price, booking_status) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booking.getRideId());
            pstmt.setInt(2, booking.getPassengerId());
            pstmt.setInt(3, booking.getSeatsBooked());
            pstmt.setDouble(4, booking.getTotalPrice());
            pstmt.setString(5, booking.getBookingStatus());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Booking> getBookingsByPassenger(int passengerId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE passenger_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, passengerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(extractBooking(rs));
                }
            }
        }
        return bookings;
    }

    public List<Booking> getBookingsByRide(int rideId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE ride_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rideId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(extractBooking(rs));
                }
            }
        }
        return bookings;
    }

    public Booking getBookingById(int bookingId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBooking(rs);
                }
            }
        }
        return null;
    }

    public void updateBookingStatus(Connection conn, int bookingId, String status) throws SQLException {
        String sql = "UPDATE bookings SET booking_status = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            pstmt.executeUpdate();
        }
    }

    private Booking extractBooking(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("id"),
                rs.getInt("ride_id"),
                rs.getInt("passenger_id"),
                rs.getInt("seats_booked"),
                rs.getDouble("total_price"),
                rs.getString("booking_status"));
    }
}
