import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        RideBookingSystem system = new RideBookingSystem();

        // Signup users
        User user1 = new User(1, "Ritik", "ritik@gmail.com", "1234");
        User user2 = new User(2, "Aman", "aman@gmail.com", "abcd");

        system.signup(user1);
        system.signup(user2);

        // Create rides
        system.createRide(1, "Jaipur", "Delhi", 5, 725.00, user1);
        system.createRide(2, "Greater Noida", "Agra", 3, 340.00, user1);
        system.createRide(3, "Gurugram", "Faridabad", 2, 180.00, user2);
        system.createRide(4, "Gurugram", "Delhi", 2, 80.00, user2);

        System.out.println("\nAll Rides:");
        system.showAllRides();

        System.out.println("\nBooking Ride...");
        system.bookRide(4, user1, 1);

        System.out.println("\nAll Bookings:");
        system.showAllBookings();

        Connection conn =   DBConnection.getConnection();

        if (conn != null) {
            System.out.println("PostgreSQL connected successfully from Java!");
        } else {
            System.out.println("Database connection failed!");
        }

    }
}

