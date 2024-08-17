package com.studyhub.controller;

import com.studyhub.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/generateToken")
    public String generateToken(@RequestParam String uid) {
        try {
            return tokenService.generateCustomToken(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating token: " + e.getMessage();
        }
    }
}
