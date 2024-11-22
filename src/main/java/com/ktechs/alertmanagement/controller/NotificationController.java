package com.ktechs.alertmanagement.controller;

import com.ktechs.alertmanagement.entity.Notification;
import com.ktechs.alertmanagement.repository.NotificationRepository;
import com.ktechs.alertmanagement.service.EmailService;
import com.ktechs.alertmanagement.util.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/notify")
public class NotificationController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> sendNotification(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody NotificationRequest request) {
        try {
            // Extract user_id from JWT
            String token = authorizationHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token).intValue();

            // Fetch recent notifications for the user
            List<Notification> recentNotifications = notificationRepository.findRecentByUserId(userId);

            // Check if the user is rate-limited (sent a notification in the last 5 minutes)
            if (isRateLimited(recentNotifications)) {
                return ResponseEntity.status(429).body("You can send notifications only once every 5 minutes.");
            }

            // Create and save the new notification
            Notification newNotification = createAndSaveNotification(userId, request);

            // Send the email
            emailService.sendEmail(
                    newNotification.getEmail(),
                    "New Notification",
                    newNotification.getMessage()
            );

            return ResponseEntity.ok("Notification sent and email delivered successfully.");
        } catch (Exception e) {
            // Log error for debugging
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while processing the request.");
        }
    }

    /**
     * Checks if the user is rate-limited based on the recent notifications.
     *
     * @param recentNotifications List of recent notifications
     * @return true if the user is rate-limited, false otherwise
     */
    private boolean isRateLimited(List<Notification> recentNotifications) {
        if (!recentNotifications.isEmpty()) {
            Notification lastNotification = recentNotifications.get(0);
            long minutesSinceLastNotification = ChronoUnit.MINUTES.between(lastNotification.getTimestamp(), LocalDateTime.now());
            return minutesSinceLastNotification < 5;
        }
        return false;
    }

    /**
     * Creates and saves a new notification in the database.
     *
     * @param userId  ID of the user
     * @param request Notification request payload
     * @return The newly created Notification entity
     */
    private Notification createAndSaveNotification(Integer userId, NotificationRequest request) {
        Notification newNotification = new Notification();
        newNotification.setUser_id(userId);
        newNotification.setEmail(request.getEmail());
        newNotification.setMessage(request.getMessage());
        newNotification.setTimestamp(LocalDateTime.now());
        return notificationRepository.save(newNotification);
    }

    @Data
    static class NotificationRequest {
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String email;
        private String message;
    }
}
