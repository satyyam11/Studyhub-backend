package com.studyhub.controller;

import com.studyhub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/hubs")
public class HubController {

    @Autowired
    private HubService hubService;

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
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating hub: " + e.getMessage());
        }
    }

    // Assign an aide to a Hub
    @PostMapping("/{hubId}/aide")
    public ResponseEntity<String> assignAide(
            @PathVariable String hubId,
            @RequestBody Map<String, String> request
    ) {
        try {
            String aideId = request.get("aideId");
            String result = hubService.assignAide(hubId, aideId);
            if (result.startsWith("Error:")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error assigning aide: " + e.getMessage());
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
