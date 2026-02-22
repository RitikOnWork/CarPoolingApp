package dao;

import model.User;
import util.DatabaseUtil;

import java.sql.*;

public class UserDAO {
    public int createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, role, rating) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getRole());
            pstmt.setDouble(3, user.getRating());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("name"), rs.getString("role"),
                            rs.getDouble("rating"));
                }
            }
        }
        return null;
    }

    public void updateRating(int userId, double newRating) throws SQLException {
        String sql = "UPDATE users SET rating = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newRating);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }
}
