import java.time.LocalDateTime;

public class Ride {

    private String source;
    private String destination;
    private int seats;
    private double fare;
    private int driverId;
    private LocalDateTime departureTime;

    public Ride(String source, String destination,
                int seats, double fare,
                int driverId, LocalDateTime departureTime) {

        this.source = source;
        this.destination = destination;
        this.seats = seats;
        this.fare = fare;
        this.driverId = driverId;
        this.departureTime = departureTime;
    }

    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public int getSeats() { return seats; }
    public double getFare() { return fare; }
    public int getDriverId() { return driverId; }
    public LocalDateTime getDepartureTime() { return departureTime; }
}
