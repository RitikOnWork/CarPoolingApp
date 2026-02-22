package dao;

import model.Ride;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RideDAO {
    public int createRide(Ride ride) throws SQLException {
        String sql = "INSERT INTO rides (driver_id, source, destination, ride_date, ride_time, available_seats, price_per_seat, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ride.getDriverId());
            pstmt.setString(2, ride.getSource());
            pstmt.setString(3, ride.getDestination());
            pstmt.setDate(4, ride.getRideDate());
            pstmt.setTime(5, ride.getRideTime());
            pstmt.setInt(6, ride.getAvailableSeats());
            pstmt.setDouble(7, ride.getPricePerSeat());
            pstmt.setString(8, ride.getStatus());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Ride> getRidesByDriver(int driverId) throws SQLException {
        List<Ride> rides = new ArrayList<>();
        String sql = "SELECT * FROM rides WHERE driver_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, driverId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rides.add(extractRide(rs));
                }
            }
        }
        return rides;
    }

    public List<Ride> searchRides(String source, String destination, Date date) throws SQLException {
        List<Ride> rides = new ArrayList<>();
        String sql = "SELECT * FROM rides WHERE source = ? AND destination = ? AND ride_date = ? AND available_seats > 0 AND status = 'AVAILABLE'";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, source);
            pstmt.setString(2, destination);
            pstmt.setDate(3, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rides.add(extractRide(rs));
                }
            }
        }
        return rides;
    }

    public void updateRideStatus(int rideId, String status) throws SQLException {
        String sql = "UPDATE rides SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, rideId);
            pstmt.executeUpdate();
        }
    }

    public void updateAvailableSeats(Connection conn, int rideId, int seatsDelta) throws SQLException {
        String sql = "UPDATE rides SET available_seats = available_seats + ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seatsDelta);
            pstmt.setInt(2, rideId);
            pstmt.executeUpdate();
        }
    }

    public Ride getRideByIdForUpdate(Connection conn, int rideId) throws SQLException {
        String sql = "SELECT * FROM rides WHERE id = ? FOR UPDATE";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rideId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractRide(rs);
                }
            }
        }
        return null;
    }

    public Ride getRideById(int rideId) throws SQLException {
        String sql = "SELECT * FROM rides WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rideId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractRide(rs);
                }
            }
        }
        return null;
    }

    private Ride extractRide(ResultSet rs) throws SQLException {
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
