package main;

import model.Booking;
import model.Ride;
import model.User;
import service.BookingService;
import service.RideService;
import service.UserService;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService = new UserService();
    private static RideService rideService = new RideService();
    private static BookingService bookingService = new BookingService();
    private static User currentUser;

    public static void main(String[] args) {
        System.out.println("=== Welcome to Ride Booking App ===");

        try {
            loginOrSignup();
            if (currentUser != null) {
                if ("DRIVER".equals(currentUser.getRole())) {
                    driverMenu();
                } else {
                    passengerMenu();
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loginOrSignup() throws SQLException {
        System.out.println("1. Login (Enter User ID)");
        System.out.println("2. Signup");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 1) {
            System.out.print("Enter User ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            currentUser = userService.getUser(id);
            if (currentUser == null) {
                System.out.println("User not found!");
            }
        } else if (choice == 2) {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Role (DRIVER/PASSENGER): ");
            String role = scanner.nextLine().toUpperCase();
            int id = userService.registerUser(name, role);
            currentUser = userService.getUser(id);
            System.out.println("Registered successfully! Your User ID is: " + id);
        }
    }

    private static void driverMenu() throws SQLException {
        while (true) {
            System.out.println("\n--- Driver Menu ---");
            System.out.println("1. Create Ride");
            System.out.println("2. View My Rides");
            System.out.println("3. Cancel Ride");
            System.out.println("4. View Bookings for My Ride");
            System.out.println("5. Exit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Source: ");
                    String src = scanner.nextLine();
                    System.out.print("Destination: ");
                    String dest = scanner.nextLine();
                    System.out.print("Date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    System.out.print("Time (HH:MM:SS): ");
                    String time = scanner.nextLine();
                    System.out.print("Seats: ");
                    int seats = Integer.parseInt(scanner.nextLine());
                    System.out.print("Price per Seat: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    rideService.createRide(currentUser.getId(), src, dest, Date.valueOf(date), Time.valueOf(time),
                            seats, price);
                    System.out.println("Ride created successfully!");
                    break;
                case 2:
                    List<Ride> rides = rideService.getMyRides(currentUser.getId());
                    for (Ride r : rides) {
                        System.out.println("ID: " + r.getId() + " | " + r.getSource() + " -> " + r.getDestination()
                                + " | " + r.getRideDate() + " " + r.getRideTime() + " | Seats: " + r.getAvailableSeats()
                                + " | Status: " + r.getStatus());
                    }
                    break;
                case 3:
                    System.out.print("Enter Ride ID to cancel: ");
                    int rId = Integer.parseInt(scanner.nextLine());
                    rideService.cancelRide(rId);
                    System.out.println("Ride cancelled!");
                    break;
                case 4:
                    System.out.print("Enter Ride ID: ");
                    int rideId = Integer.parseInt(scanner.nextLine());
                    List<Booking> bookings = bookingService.getRideBookings(rideId);
                    for (Booking b : bookings) {
                        System.out.println("Booking ID: " + b.getId() + " | Passenger: " + b.getPassengerId()
                                + " | Seats: " + b.getSeatsBooked() + " | Total: " + b.getTotalPrice() + " | Status: "
                                + b.getBookingStatus());
                    }
                    break;
                case 5:
                    return;
            }
        }
    }

    private static void passengerMenu() throws SQLException {
        while (true) {
            System.out.println("\n--- Passenger Menu ---");
            System.out.println("1. Search Ride");
            System.out.println("2. Book Seat(s)");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View My Bookings");
            System.out.println("5. Rate Driver");
            System.out.println("6. Exit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Source: ");
                    String src = scanner.nextLine();
                    System.out.print("Destination: ");
                    String dest = scanner.nextLine();
                    System.out.print("Date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    List<Ride> rides = rideService.searchRides(src, dest, Date.valueOf(date));
                    for (Ride r : rides) {
                        System.out.println("ID: " + r.getId() + " | " + r.getSource() + " -> " + r.getDestination()
                                + " | Time: " + r.getRideTime() + " | Seats: " + r.getAvailableSeats() + " | Price: "
                                + r.getPricePerSeat());
                    }
                    break;
                case 2:
                    System.out.print("Enter Ride ID: ");
                    int rid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Seats to book: ");
                    int seats = Integer.parseInt(scanner.nextLine());
                    if (bookingService.bookRide(rid, currentUser.getId(), seats)) {
                        System.out.println("Booking successful!");
                    }
                    break;
                case 3:
                    System.out.print("Enter Booking ID to cancel: ");
                    int bId = Integer.parseInt(scanner.nextLine());
                    bookingService.cancelBooking(bId);
                    System.out.println("Booking cancelled!");
                    break;
                case 4:
                    List<Booking> bookings = bookingService.getMyBookings(currentUser.getId());
                    for (Booking b : bookings) {
                        System.out.println("Booking ID: " + b.getId() + " | Ride: " + b.getRideId() + " | Seats: "
                                + b.getSeatsBooked() + " | Status: " + b.getBookingStatus());
                    }
                    break;
                case 5:
                    System.out.print("Enter Driver ID: ");
                    int dId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Rating (1-5): ");
                    int stars = Integer.parseInt(scanner.nextLine());
                    userService.rateDriver(dId, stars);
                    System.out.println("Rating submitted!");
                    break;
                case 6:
                    return;
            }
        }
    }
}
