package com.studyhub.controller;

import com.studyhub.service.UserHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/userHubs")
public class UserHubController {

    @Autowired
    private UserHubService userHubService;

    // Get the list of Hubs a user has joined
    @GetMapping("/{userId}")
    public ResponseEntity<List<String>> getJoinedHubs(@PathVariable String userId) {
        try {
            List<String> hubs = userHubService.getJoinedHubs(userId);
            return ResponseEntity.ok(hubs);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Add a Hub to a user's joined list
    @PostMapping("/{userId}")
    public ResponseEntity<String> addHubToUser(@PathVariable String userId, @RequestParam String hubId) {
        try {
            userHubService.addHubToUser(userId, hubId);
            return ResponseEntity.ok("Hub added successfully.");
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding hub: " + e.getMessage());
        }
    }
}
