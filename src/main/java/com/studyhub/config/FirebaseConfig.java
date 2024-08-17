package com.studyhub.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        // Replace with your absolute path to the serviceAccountKey.json file
        String absolutePath = "C:/Program Files/Java/studyhub/src/main/resources/serviceAccountKey.json";

        FileInputStream serviceAccount = new FileInputStream(absolutePath);

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        // Initialize FirebaseApp if it is not already initialized
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirebaseAuth.getInstance();
    }
}
