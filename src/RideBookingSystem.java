import java.util.ArrayList;
import java.util.List;

public class RideBookingSystem {

    private List<User> users = new ArrayList<>();
    private List<Ride> rideList = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    private int bookingCounter = 1;

    // SIGNUP
    public void signup(User user) {
        users.add(user);
        System.out.println("Signup successful: " + user.getName());
    }

    // CREATE RIDE
    public void createRide(int id, String source, String destination,
                           int seats, double fare, User user) {

        Ride ride = new Ride(id, source, destination, seats, fare, user);
        rideList.add(ride);
        System.out.println("Ride Created: " + ride);
    }

    // SHOW ALL RIDES
    public void showAllRides() {
        for (Ride ride : rideList) {
            System.out.println(ride);
        }
    }

    // SEARCH RIDE
    public List<Ride> searchRide(String source, String destination, int seats) {
        List<Ride> result = new ArrayList<>();

        for (Ride ride : rideList) {
            if (ride.getSource().equalsIgnoreCase(source)
                    && ride.getDestination().equalsIgnoreCase(destination)
                    && ride.getSeats() >= seats) {
                result.add(ride);
            }
        }
        return result;
    }

    // BOOK RIDE
    public void bookRide(int rideId, User user, int seatsBooked) {

        for (Ride ride : rideList) {
            if (ride.getId() == rideId) {

                if (ride.getSeats() < seatsBooked) {
                    System.out.println("❌ Not enough seats available");
                    return;
                }

                ride.setSeats(ride.getSeats() - seatsBooked);

                Booking booking = new Booking(
                        bookingCounter++,
                        ride,
                        user,
                        seatsBooked
                );

                bookings.add(booking);
                System.out.println("Booking Successful");
                System.out.println(booking);
                return;
            }
        }
        System.out.println("Ride not found");
    }

    // SHOW ALL BOOKINGS
    public void showAllBookings() {
        for (Booking booking : bookings) {
            System.out.println(booking);
        }
    }
}
