package com.radioheaven.radioheaven_backend.config;

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
        // Get service account key path from environment variable, with fallback to default location
        String serviceAccountPath = System.getenv("FIREBASE_KEY_PATH");
        if (serviceAccountPath == null || serviceAccountPath.isEmpty()) {
            serviceAccountPath = "config/serviceAccountKey.json";
        }

        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId("radio-heaven-777")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
        return FirebaseAuth.getInstance();
    }
}