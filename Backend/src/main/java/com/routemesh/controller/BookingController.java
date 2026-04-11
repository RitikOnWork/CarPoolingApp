package com.routemesh.controller;

import com.routemesh.model.Booking;
import com.routemesh.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> bookRide(@RequestBody Map<String, Object> request) {
        try {
            boolean success = bookingService.bookRide(
                    ((Number) request.get("rideId")).intValue(),
                    ((Number) request.get("passengerId")).intValue(),
                    ((Number) request.get("seats")).intValue()
            );
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Booking successful"));
            }
            return ResponseEntity.badRequest().body(Map.of("error", "Booking failed. Check availability."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/passenger/{passengerId}")
    public List<Booking> getMyBookings(@PathVariable int passengerId) {
        return bookingService.getMyBookings(passengerId);
    }

    @GetMapping("/ride/{rideId}")
    public List<Booking> getRideBookings(@PathVariable int rideId) {
        return bookingService.getRideBookings(rideId);
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable int bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(Map.of("message", "Booking cancelled"));
    }
}
