package com.routemesh.controller;

import com.routemesh.model.User;
import com.routemesh.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Backend is UP and Running!");
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            int id = userService.registerUser(
                    request.get("name"),
                    request.get("email"),
                    request.get("password"),
                    request.get("role")
            );
            return ResponseEntity.ok(Map.of("id", id, "message", "User registered successfully"));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered. Try a different one."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Registration failed: " + e.getMessage()));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        User user = userService.login(request.get("email"), request.get("password"));
        if (user != null) {
            // In a real app, you'd return a JWT here
            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "role", user.getRole(),
                    "rating", user.getAverageRating()
            ));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
}
