package com.kfleetguard.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseService {
    private DatabaseReference databaseReference;
    private boolean firebaseEnabled = false;

    @PostConstruct
    public void init() {
        try {
            Dotenv dotenv = Dotenv.load();
            String databaseUrl = dotenv.get("FIREBASE_DATABASE_URL");
            
            if (databaseUrl == null || databaseUrl.contains("your-firebase-db-url")) {
                System.out.println("⚠️  Firebase not configured. Running in DEMO mode.");
                System.out.println("📝 To enable Firebase:");
                System.out.println("   1. Add your Firebase credentials to src/main/resources/firebase-config.json");
                System.out.println("   2. Set FIREBASE_DATABASE_URL in .env file");
                return;
            }
            
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-config.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            databaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseEnabled = true;
            System.out.println("✅ Firebase connected successfully!");
        } catch (Exception e) {
            System.out.println("⚠️  Firebase connection failed. Running in DEMO mode.");
            System.out.println("📝 Error: " + e.getMessage());
        }
    }

    public DatabaseReference getDatabase() {
        if (!firebaseEnabled) {
            throw new RuntimeException("Firebase not configured. Please set up Firebase credentials.");
        }
        return databaseReference;
    }
    
    public boolean isFirebaseEnabled() {
        return firebaseEnabled;
    }
} 