package com.routemesh.dao;

import com.routemesh.model.Booking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BookingDAO {
    private final JdbcTemplate jdbcTemplate;

    public BookingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (ride_id, passenger_id, seats_booked, total_price, booking_status) VALUES (?, ?, ?, ?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Integer.class,
                booking.getRideId(), booking.getPassengerId(), booking.getSeatsBooked(),
                booking.getTotalPrice(), booking.getBookingStatus());
    }

    public List<Booking> getBookingsByPassenger(int passengerId) {
        String sql = "SELECT * FROM bookings WHERE passenger_id = ?";
        return jdbcTemplate.query(sql, new BookingRowMapper(), passengerId);
    }

    public List<Booking> getBookingsByRide(int rideId) {
        String sql = "SELECT * FROM bookings WHERE ride_id = ?";
        return jdbcTemplate.query(sql, new BookingRowMapper(), rideId);
    }

    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BookingRowMapper(), bookingId);
    }

    public void updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET booking_status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, bookingId);
    }

    private static class BookingRowMapper implements RowMapper<Booking> {
        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Booking(
                    rs.getInt("id"),
                    rs.getInt("ride_id"),
                    rs.getInt("passenger_id"),
                    rs.getInt("seats_booked"),
                    rs.getDouble("total_price"),
                    rs.getString("booking_status"));
        }
    }
}
