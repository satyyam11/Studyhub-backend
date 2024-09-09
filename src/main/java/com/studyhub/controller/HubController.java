package com.studyhub.controller;

import com.studyhub.service.HubService;
import com.studyhub.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/hubs")
public class HubController {

    @Autowired
    private HubService hubService;

    @Autowired
    private RoleService roleService;

    // Create a new Hub
    @PostMapping
    public ResponseEntity<String> createHub(
            @RequestParam String name,
            @RequestParam String creatorId,
            @RequestParam boolean isPublic
    ) {
        try {
            String result = hubService.createHub(name, creatorId, isPublic);
            if (result.startsWith("Error:")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            // Extract the hubId from the result message
            String hubId = result.split(": ")[1];
            // Assign the creator role
            roleService.assignCreatorRole(hubId, creatorId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating hub: " + e.getMessage());
        }
    }

    // Get a Hub by ID
    @GetMapping("/{hubId}")
    public ResponseEntity<Map<String, Object>> getHubById(@PathVariable String hubId) {
        try {
            Map<String, Object> hub = hubService.getHubById(hubId);
            if (hub.containsKey("error")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(hub);
            }
            return ResponseEntity.ok(hub);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getHubsForUser(@PathVariable String userId) {
        try {
            List<Map<String, Object>> userHubs = hubService.getHubsForUser(userId);
            if (userHubs.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("data", userHubs);
                response.put("message", "No hubs found for this user.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("data", userHubs);
            response.put("message", "Hubs fetched successfully.");
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Error fetching hubs: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // Update a Hub by ID
    @PutMapping("/{hubId}")
    public ResponseEntity<String> updateHub(
            @PathVariable String hubId,
            @RequestParam String name,
            @RequestParam boolean isPublic
    ) {
        try {
            String result = hubService.updateHub(hubId, name, isPublic);
            if (result.startsWith("Error:")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating hub: " + e.getMessage());
        }
    }

    // Delete a Hub by ID
    @DeleteMapping("/{hubId}")
    public ResponseEntity<String> deleteHub(@PathVariable String hubId) {
        try {
            String result = hubService.deleteHub(hubId);
            if (result.startsWith("Error:")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting hub: " + e.getMessage());
        }
    }
}
