package com.studyhub.controller;

import com.studyhub.service.TokenService;
import com.studyhub.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/generate")
    public ResponseEntity<String> generateToken(@RequestParam String uid) {
        try {
            String currentUid = SecurityUtil.getCurrentUserUid();
            if (currentUid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            // For testing, allow generating token only for yourself
            if (!currentUid.equals(uid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only generate tokens for yourself");
            }
            String token = tokenService.generateCustomToken(uid);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating token: " + e.getMessage());
        }
    }
}
