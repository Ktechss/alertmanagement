package com.ktechs.alertmanagement.controller;

import com.ktechs.alertmanagement.entity.User;
import com.ktechs.alertmanagement.repository.UserRepository;
import com.ktechs.alertmanagement.util.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(produces = "application/json") // Explicitly specify JSON response
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if (user.isEmpty() || !user.get().getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body(new AuthResponse(null, "Invalid credentials", 0));
        }

        Long userId = user.get().getId();
        String token = jwtUtil.generateToken(userId);
        long expirationTime = 1800000; // 30 minutes

        return ResponseEntity.ok(new AuthResponse(token, "Login successful", expirationTime));
    }

    public static class AuthRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    static class AuthResponse {
        private String token;
        private String message;
        private long expiresIn;

        public AuthResponse(String token, String message, long expiresIn) {
            this.token = token;
            this.message = message;
            this.expiresIn = expiresIn;
        }

        // Getters and Setters
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }
    }
}
