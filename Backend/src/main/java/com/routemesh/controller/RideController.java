package com.routemesh.controller;

import com.routemesh.model.Ride;
import com.routemesh.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "*")
public class RideController {
    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public ResponseEntity<?> createRide(@RequestBody Map<String, Object> request) {
        try {
            int id = rideService.createRide(
                    ((Number) request.get("driverId")).intValue(),
                    (String) request.get("source"),
                    (String) request.get("destination"),
                    Date.valueOf((String) request.get("date")),
                    Time.valueOf((String) request.get("time")),
                    ((Number) request.get("seats")).intValue(),
                    Double.parseDouble(request.get("price").toString())
            );
            return ResponseEntity.ok(Map.of("id", id, "message", "Ride created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/search")
    public List<Ride> searchRides(@RequestParam String source, @RequestParam String destination, @RequestParam String date) {
        return rideService.searchRides(source, destination, Date.valueOf(date));
    }

    @GetMapping("/driver/{driverId}")
    public List<Ride> getMyRides(@PathVariable int driverId) {
        return rideService.getMyRides(driverId);
    }

    @PutMapping("/{rideId}/cancel")
    public ResponseEntity<?> cancelRide(@PathVariable int rideId) {
        rideService.cancelRide(rideId);
        return ResponseEntity.ok(Map.of("message", "Ride cancelled"));
    }
}
