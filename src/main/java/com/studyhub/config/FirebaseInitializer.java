package com.studyhub.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseInitializer {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new ClassPathResource("firebase-adminsdk.json").getInputStream());

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .setStorageBucket("studyhub-db.appspot.com") // Ensure this is correct
                        .build();

                return FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                throw new IOException("Failed to initialize Firebase", e);
            }
        }
        return FirebaseApp.getInstance();
    }
}
