package com.studyhub.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public String generateCustomToken(String uid) throws FirebaseAuthException {
        // Generate a custom token
        return FirebaseAuth.getInstance().createCustomToken(uid);
    }
}
