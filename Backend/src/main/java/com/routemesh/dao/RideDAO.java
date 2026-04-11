package com.routemesh.dao;

import com.routemesh.model.Ride;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RideDAO {
    private final JdbcTemplate jdbcTemplate;

    public RideDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createRide(Ride ride) {
        String sql = "INSERT INTO rides (driver_id, source, destination, ride_date, ride_time, available_seats, price_per_seat, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Integer.class,
                ride.getDriverId(), ride.getSource(), ride.getDestination(),
                ride.getRideDate(), ride.getRideTime(), ride.getAvailableSeats(),
                ride.getPricePerSeat(), ride.getStatus());
    }

    public List<Ride> getRidesByDriver(int driverId) {
        String sql = "SELECT * FROM rides WHERE driver_id = ?";
        return jdbcTemplate.query(sql, new RideRowMapper(), driverId);
    }

    public List<Ride> searchRides(String source, String destination, java.sql.Date date) {
        String sql = "SELECT * FROM rides WHERE source = ? AND destination = ? AND ride_date = ? AND available_seats > 0 AND status = 'AVAILABLE'";
        return jdbcTemplate.query(sql, new RideRowMapper(), source, destination, date);
    }

    public void updateRideStatus(int rideId, String status) {
        String sql = "UPDATE rides SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, rideId);
    }

    public void updateAvailableSeats(int rideId, int seatsDelta) {
        String sql = "UPDATE rides SET available_seats = available_seats + ? WHERE id = ?";
        jdbcTemplate.update(sql, seatsDelta, rideId);
    }

    public Ride getRideById(int rideId) {
        String sql = "SELECT * FROM rides WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new RideRowMapper(), rideId);
    }

    private static class RideRowMapper implements RowMapper<Ride> {
        @Override
        public Ride mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Ride(
                    rs.getInt("id"),
                    rs.getInt("driver_id"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getDate("ride_date"),
                    rs.getTime("ride_time"),
                    rs.getInt("available_seats"),
                    rs.getDouble("price_per_seat"),
                    rs.getString("status"));
        }
    }
}
