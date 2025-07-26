package com.kfleetguard.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.kfleetguard.firebase.FirebaseService;
import com.kfleetguard.model.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {
    @Autowired
    private FirebaseService firebaseService;
    
    public List<Alert> getAlertsForVehicle(String vehicleId) {
        List<Alert> alerts = new ArrayList<>();
        
        if (!firebaseService.isFirebaseEnabled()) {
            System.out.println("📝 Firebase not configured. Returning empty alerts list.");
            return alerts;
        }
        
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("alerts").child(vehicleId);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Alert alert = snapshot.getValue(Alert.class);
                        alerts.add(alert);
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    System.out.println("❌ Firebase alert fetch failed: " + error.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("❌ Error fetching alerts: " + e.getMessage());
        }
        
        return alerts;
    }
} 