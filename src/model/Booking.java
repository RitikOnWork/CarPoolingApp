package model;

public class Booking {
    private int id;
    private int rideId;
    private int passengerId;
    private int seatsBooked;
    private double totalPrice;
    private String bookingStatus; // e.g., CONFIRMED, CANCELLED

    public Booking() {}

    public Booking(int id, int rideId, int passengerId, int seatsBooked, double totalPrice, String bookingStatus) {
        this.id = id;
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.seatsBooked = seatsBooked;
        this.totalPrice = totalPrice;
        this.bookingStatus = bookingStatus;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRideId() { return rideId; }
    public void setRideId(int rideId) { this.rideId = rideId; }

    public int getPassengerId() { return passengerId; }
    public void setPassengerId(int passengerId) { this.passengerId = passengerId; }

    public int getSeatsBooked() { return seatsBooked; }
    public void setSeatsBooked(int seatsBooked) { this.seatsBooked = seatsBooked; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
}
