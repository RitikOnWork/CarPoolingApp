import java.sql.*;
import java.time.LocalDateTime;

public class RideBookingSystem {

    // SIGNUP
    public void signup(User user) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "INSERT INTO users(name,email,password,role) VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());

            ps.executeUpdate();
            System.out.println("User Registered Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ADD VEHICLE
    public void addVehicle(int driverId, Vehicle vehicle) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "INSERT INTO vehicles(driver_id,vehicle_number,model,color,capacity) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, driverId);
            ps.setString(2, vehicle.getVehicleNumber());
            ps.setString(3, vehicle.getModel());
            ps.setString(4, vehicle.getColor());
            ps.setInt(5, vehicle.getCapacity());

            ps.executeUpdate();
            System.out.println("Vehicle Added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VERIFY DRIVER
    public void verifyDriver(int driverId, String license) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "UPDATE users SET is_verified=TRUE, license_number=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, license);
            ps.setInt(2, driverId);

            ps.executeUpdate();
            System.out.println("Driver Verified");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CREATE RIDE
    public void createRide(Ride ride) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "INSERT INTO rides(source,destination,seats,fare,driver_id,departure_time,status) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, ride.getSource());
            ps.setString(2, ride.getDestination());
            ps.setInt(3, ride.getSeats());
            ps.setDouble(4, ride.getFare());
            ps.setInt(5, ride.getDriverId());
            ps.setTimestamp(6, Timestamp.valueOf(ride.getDepartureTime()));
            ps.setString(7, "SCHEDULED");

            ps.executeUpdate();
            System.out.println("Ride Created");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // BOOK RIDE (WITH TRANSACTION)
    public void bookRide(int rideId, int passengerId, int seatsBooked) {

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            String checkSql = "SELECT seats,fare FROM rides WHERE id=?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setInt(1, rideId);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {

                int availableSeats = rs.getInt("seats");
                double fare = rs.getDouble("fare");

                if (availableSeats < seatsBooked) {
                    System.out.println("Not enough seats");
                    conn.rollback();
                    return;
                }

                String updateSql = "UPDATE rides SET seats=seats-? WHERE id=?";
                PreparedStatement updatePs = conn.prepareStatement(updateSql);
                updatePs.setInt(1, seatsBooked);
                updatePs.setInt(2, rideId);
                updatePs.executeUpdate();

                String insertSql = "INSERT INTO bookings(ride_id,passenger_id,seats_booked,total_fare) VALUES(?,?,?,?)";
                PreparedStatement insertPs = conn.prepareStatement(insertSql);
                insertPs.setInt(1, rideId);
                insertPs.setInt(2, passengerId);
                insertPs.setInt(3, seatsBooked);
                insertPs.setDouble(4, seatsBooked * fare);
                insertPs.executeUpdate();

                conn.commit();
                System.out.println("Booking Successful");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // RATE DRIVER
    public void rateDriver(int driverId, int stars) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql = """
                    UPDATE users
                    SET average_rating =
                    ((average_rating * total_ratings + ?) / (total_ratings + 1)),
                    total_ratings = total_ratings + 1
                    WHERE id = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, stars);
            ps.setInt(2, driverId);
            ps.executeUpdate();

            System.out.println("Driver Rated");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public void searchRide(String source, String destination, int requiredSeats) {

    try (Connection conn = DBConnection.getConnection()) {

        String sql = """
                SELECT r.id, r.source, r.destination,
                       r.seats, r.fare, r.departure_time,
                       u.name AS driver_name, u.average_rating
                FROM rides r
                JOIN users u ON r.driver_id = u.id
                WHERE r.source = ?
                AND r.destination = ?
                AND r.seats >= ?
                AND r.status = 'SCHEDULED'
                """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, source);
        ps.setString(2, destination);
        ps.setInt(3, requiredSeats);

        ResultSet rs = ps.executeQuery();

        boolean found = false;

        while (rs.next()) {
            found = true;

            System.out.println("Ride ID: " + rs.getInt("id"));
            System.out.println("Route: " +
                    rs.getString("source") + " -> " +
                    rs.getString("destination"));
            System.out.println("Seats Available: " + rs.getInt("seats"));
            System.out.println("Fare: ₹" + rs.getDouble("fare"));
            System.out.println("Departure: " + rs.getTimestamp("departure_time"));
            System.out.println("Driver: " + rs.getString("driver_name"));
            System.out.println("Driver Rating: " + rs.getDouble("average_rating"));
            System.out.println("---------------------------");
        }

        if (!found) {
            System.out.println("No matching rides found.");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void cancelBooking(int bookingId) {

    try (Connection conn = DBConnection.getConnection()) {

        conn.setAutoCommit(false);

        // Step 1: Get booking details
        String getBookingSql =
                "SELECT ride_id, seats_booked FROM bookings WHERE id = ?";

        PreparedStatement getBookingPs =
                conn.prepareStatement(getBookingSql);
        getBookingPs.setInt(1, bookingId);

        ResultSet rs = getBookingPs.executeQuery();

        if (!rs.next()) {
            System.out.println("Booking not found.");
            conn.rollback();
            return;
        }

        int rideId = rs.getInt("ride_id");
        int seatsBooked = rs.getInt("seats_booked");

        // Step 2: Restore seats
        String updateRideSql =
                "UPDATE rides SET seats = seats + ? WHERE id = ?";

        PreparedStatement updateRidePs =
                conn.prepareStatement(updateRideSql);
        updateRidePs.setInt(1, seatsBooked);
        updateRidePs.setInt(2, rideId);
        updateRidePs.executeUpdate();

        // Step 3: Delete booking
        String deleteBookingSql =
                "DELETE FROM bookings WHERE id = ?";

        PreparedStatement deleteBookingPs =
                conn.prepareStatement(deleteBookingSql);
        deleteBookingPs.setInt(1, bookingId);
        deleteBookingPs.executeUpdate();

        conn.commit();

        System.out.println("Booking Cancelled Successfully.");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
