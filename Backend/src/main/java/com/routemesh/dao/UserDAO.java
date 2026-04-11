package com.routemesh.dao;

import com.routemesh.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createUser(User user) {
        String sql = "INSERT INTO users (name, email, password, role, average_rating, total_ratings) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Integer.class,
                user.getName(), user.getEmail(), user.getPassword(),
                user.getRole(), user.getAverageRating(), user.getTotalRatings());
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
        } catch (Exception e) {
            return null;
        }
    }

    public void updateRating(int userId, double newRating, int totalRatings) {
        String sql = "UPDATE users SET average_rating = ?, total_ratings = ? WHERE id = ?";
        jdbcTemplate.update(sql, newRating, totalRatings, userId);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getDouble("average_rating"),
                    rs.getInt("total_ratings"));
        }
    }
}
