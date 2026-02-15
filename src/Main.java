import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        RideBookingSystem system = new RideBookingSystem();

        // Create users
        system.signup(new User("Ritik", "ritik@gmail.com", "1234", "DRIVER"));
        system.signup(new User("Aman", "aman@gmail.com", "abcd", "PASSENGER"));

        // Add vehicle for driver (assuming driver id = 1)
        system.addVehicle(1,
                new Vehicle("RJ14AB1234", "Hyundai i20", "White", 5));

        // Verify driver
        system.verifyDriver(1, "DL-99887766");

        // Create ride
        system.createRide(
                new Ride("Jaipur", "Delhi", 4, 700,
                        1, LocalDateTime.now().plusHours(5)));

        // Book ride
        system.bookRide(1, 2, 2);

        // Rate driver
        system.rateDriver(1, 5);

        System.out.println("\nSearching Ride:");
        system.searchRide("Jaipur", "Delhi", 1);

        // Cancel booking with ID = 1
        system.cancelBooking(1);


    }
}
