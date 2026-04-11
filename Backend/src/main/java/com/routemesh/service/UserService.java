package com.routemesh.service;

import com.routemesh.dao.UserDAO;
import com.routemesh.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserDAO userDAO, BCryptPasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public int registerUser(String name, String email, String password, String role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setAverageRating(5.0);
        user.setTotalRatings(0);
        return userDAO.createUser(user);
    }

    public User login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User getUser(int id) {
        return userDAO.getUserById(id);
    }

    public void rateDriver(int driverId, int stars) {
        User driver = userDAO.getUserById(driverId);
        if (driver != null && "DRIVER".equals(driver.getRole())) {
            int newTotal = driver.getTotalRatings() + 1;
            double newRating = ((driver.getAverageRating() * driver.getTotalRatings()) + stars) / newTotal;
            userDAO.updateRating(driverId, newRating, newTotal);
        }
    }
}
