public class Ride {

    private int id;
    private String source;
    private String destination;
    private int seats;
    private double fare;
    private User driver;

    public Ride(int id, String source, String destination,
                int seats, double fare, User driver) {

        this.id = id;
        this.source = source;
        this.destination = destination;
        this.seats = seats;
        this.fare = fare;
        this.driver = driver;
    }

    public int getId() { return id; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public int getSeats() { return seats; }
    public double getFare() { return fare; }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Ride{id=" + id +
                ", " + source + " -> " + destination +
                ", seats=" + seats +
                ", fare=" + fare +
                ", driver=" + driver.getName() +
                "}";
    }
}
