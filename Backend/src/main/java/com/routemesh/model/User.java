package com.routemesh.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role; // DRIVER or PASSENGER
    private double averageRating;
    private int totalRatings;

    public User() {}

    public User(int id, String name, String email, String password, String role, double averageRating, int totalRatings) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.averageRating = averageRating;
        this.totalRatings = totalRatings;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public int getTotalRatings() { return totalRatings; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }
}


