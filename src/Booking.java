public class Booking {

    private int rideId;
    private int passengerId;
    private int seatsBooked;
    private double totalFare;

    public Booking(int rideId, int passengerId,
                   int seatsBooked, double totalFare) {

        this.rideId = rideId;
        this.passengerId = passengerId;
        this.seatsBooked = seatsBooked;
        this.totalFare = totalFare;
    }
}
