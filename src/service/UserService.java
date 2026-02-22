package service;

import dao.UserDAO;
import model.User;

import java.sql.SQLException;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public int registerUser(String name, String role) throws SQLException {
        User user = new User();
        user.setName(name);
        user.setRole(role);
        user.setRating(5.0); // Default rating
        return userDAO.createUser(user);
    }

    public User getUser(int id) throws SQLException {
        return userDAO.getUserById(id);
    }

    public void rateDriver(int driverId, int stars) throws SQLException {
        User driver = userDAO.getUserById(driverId);
        if (driver != null && "DRIVER".equals(driver.getRole())) {
            double newRating = (driver.getRating() + stars) / 2.0; // Simple averaging for this demo
            userDAO.updateRating(driverId, newRating);
        }
    }
}
