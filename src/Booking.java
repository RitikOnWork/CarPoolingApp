public class Booking {

    private int bookingId;
    private Ride ride;
    private User user;
    private int seatsBooked;
    private double totalFare;

    public Booking(int bookingId, Ride ride, User user, int seatsBooked) {
        this.bookingId = bookingId;
        this.ride = ride;
        this.user = user;
        this.seatsBooked = seatsBooked;
        this.totalFare = seatsBooked * ride.getFare();
    }

    @Override
    public String toString() {
        return "Booking{id=" + bookingId +
                ", user=" + user.getName() +
                ", ride=" + ride.getSource() + "->" + ride.getDestination() +
                ", seats=" + seatsBooked +
                ", totalFare=" + totalFare +
                "}";
    }
}
