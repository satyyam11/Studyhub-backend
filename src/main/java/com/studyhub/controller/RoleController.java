package com.studyhub.controller;

import com.studyhub.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/hubs")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/{hubId}/addMember")
    public ResponseEntity<String> addMember(@PathVariable String hubId, @RequestParam String userId) {
        try {
            roleService.addMember(hubId, userId);
            return ResponseEntity.ok("User added as member");
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding member: " + e.getMessage());
        }
    }

    @PostMapping("/{hubId}/promoteToAide")
    public ResponseEntity<String> promoteToAide(@PathVariable String hubId, @RequestParam String userId) {
        try {
            roleService.promoteToAide(hubId, userId);
            return ResponseEntity.ok("User promoted to aide");
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error promoting to aide: " + e.getMessage());
        }
    }

    @PutMapping("/{hubId}/reassignRole")
    public ResponseEntity<String> reassignRole(
            @PathVariable String hubId,
            @RequestParam String userId,
            @RequestParam String newRole
    ) {
        try {
            roleService.reassignRole(hubId, userId, newRole);
            return ResponseEntity.ok("Role reassigned successfully");
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reassigning role: " + e.getMessage());
        }
    }

    @DeleteMapping("/{hubId}/deleteRole")
    public ResponseEntity<String> deleteRole(@PathVariable String hubId, @RequestParam String userId) {
        try {
            roleService.deleteRole(hubId, userId);
            return ResponseEntity.ok("Role deleted successfully");
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting role: " + e.getMessage());
        }
    }

    @GetMapping("/{hubId}/role/{userId}")
    public ResponseEntity<String> getRole(@PathVariable String hubId, @PathVariable String userId) {
        try {
            String role = roleService.getRole(hubId, userId);
            return role != null ? ResponseEntity.ok("Role: " + role) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching role: " + e.getMessage());
        }
    }
}
