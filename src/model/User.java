package model;

public class User {
    private int id;
    private String name;
    private String role; // DRIVER or PASSENGER
    private double rating;

    public User() {}

    public User(int id, String name, String role, double rating) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.rating = rating;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
