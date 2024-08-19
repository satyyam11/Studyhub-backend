package com.studyhub.controller;

import com.studyhub.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hubs")
public class HubController {

    @Autowired
    private HubService hubService;

    /**
     * Creates a new Hub.
     *
     * @param name       The name of the Hub.
     * @param creatorId  The ID of the user creating the Hub.
     * @param isPublic   Whether the Hub is public or private.
     * @return           A response entity with the result of the creation.
     */
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

    /**
     * Retrieves a Hub by its ID.
     *
     * @param id The ID of the Hub to retrieve.
     * @return   A response entity with the Hub data or an error message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getHubById(@PathVariable String id) {
        try {
            Map<String, Object> hub = hubService.getHubById(id);
            if (hub.containsKey("error")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(hub);
            }
            return ResponseEntity.ok(hub);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error retrieving hub: " + e.getMessage()));
        }
    }

    /**
     * Updates an existing Hub.
     *
     * @param id         The ID of the Hub to update.
     * @param name       The new name of the Hub.
     * @param isPublic   Whether the Hub is public or private.
     * @return           A response entity with the result of the update.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateHub(
            @PathVariable String id,
            @RequestParam String name,
            @RequestParam boolean isPublic
    ) {
        try {
            String result = hubService.updateHub(id, name, isPublic);
            if (result.startsWith("Error:")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating hub: " + e.getMessage());
        }
    }

    /**
     * Deletes a Hub by its ID.
     *
     * @param id The ID of the Hub to delete.
     * @return   A response entity with the result of the deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHub(@PathVariable String id) {
        try {
            String result = hubService.deleteHub(id);
            if (result.startsWith("Error:")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting hub: " + e.getMessage());
        }
    }
}
