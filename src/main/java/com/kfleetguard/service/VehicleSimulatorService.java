package com.kfleetguard.service;

import com.google.firebase.database.DatabaseReference;
import com.kfleetguard.firebase.FirebaseService;
import com.kfleetguard.model.Alert;
import com.kfleetguard.model.Location;
import com.kfleetguard.model.Telemetry;
import com.kfleetguard.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VehicleSimulatorService {
    @Autowired
    private FirebaseService firebaseService;

    private final List<Vehicle> vehicles = new ArrayList<>();
    private final Random random = new Random();
    private final String[] routes = {"Depot → Zone A", "Depot → Zone B", "Zone A → Zone B", "Zone B → Zone C", "Zone C → Depot"};
    private final Map<String, String> vehicleRoutes = new HashMap<>();
    private final Map<String, Location> expectedLocations = new HashMap<>();

    @PostConstruct
    public void initVehicles() {
        for (int i = 1; i <= 5; i++) {
            String vehicleId = "truck-00" + i;
            Location startLocation = new Location(12.9716 + random.nextDouble() * 0.1, 77.5946 + random.nextDouble() * 0.1);
            String route = routes[random.nextInt(routes.length)];
            
            vehicles.add(new Vehicle(
                    vehicleId,
                    startLocation,
                    new Telemetry(50 + random.nextDouble() * 30, 100, "running", route),
                    null
            ));
            
            vehicleRoutes.put(vehicleId, route);
            expectedLocations.put(vehicleId, startLocation);
        }
        System.out.println("🚗 Initialized " + vehicles.size() + " vehicles for simulation");
    }

    @Scheduled(fixedRate = 1000) // Changed from 5000 to 1000 (1 second)
    public void simulateAndSend() {
        for (Vehicle v : vehicles) {
            // Simulate realistic vehicle movement
            simulateVehicleMovement(v);
            
            // Update telemetry
            updateTelemetry(v);
            
            // Check for alerts
            List<Alert> alerts = checkForAlerts(v);
            
            // Send to Firebase if enabled
            if (firebaseService.isFirebaseEnabled()) {
                try {
                    DatabaseReference db = firebaseService.getDatabase();
                    if (!alerts.isEmpty()) {
                        for (Alert alert : alerts) {
                            db.child("alerts").child(v.getVehicleId()).child(alert.getType()).setValueAsync(alert);
                        }
                    }
                    db.child("vehicles").child(v.getVehicleId()).setValueAsync(v);
                } catch (Exception e) {
                    System.out.println("❌ Firebase update failed: " + e.getMessage());
                }
            }
        }
        System.out.println("📊 Updated " + vehicles.size() + " vehicles with enhanced simulation");
    }
    
    private void simulateVehicleMovement(Vehicle vehicle) {
        // Simulate realistic GPS movement with larger changes
        double latChange = (random.nextDouble() - 0.5) * 0.02; // Increased from 0.01
        double lonChange = (random.nextDouble() - 0.5) * 0.02; // Increased from 0.01
        
        vehicle.getLocation().setLat(vehicle.getLocation().getLat() + latChange);
        vehicle.getLocation().setLon(vehicle.getLocation().getLon() + lonChange);
        
        // Update expected location based on route
        updateExpectedLocation(vehicle);
    }
    
    private void updateTelemetry(Vehicle vehicle) {
        Telemetry telemetry = vehicle.getTelemetry();
        
        // More dramatic speed changes
        double currentSpeed = telemetry.getSpeed();
        double speedChange = (random.nextDouble() - 0.5) * 40; // Increased from 20
        telemetry.setSpeed(Math.max(0, Math.min(120, currentSpeed + speedChange)));
        
        // Reduced fuel consumption (much less aggressive)
        double fuelConsumption = telemetry.getSpeed() > 80 ? 0.2 : 0.1; // Reduced from 4.0/2.0 to 0.2/0.1
        telemetry.setFuelLevel(Math.max(0, telemetry.getFuelLevel() - fuelConsumption * random.nextDouble()));
        
        // Fuel refill logic (when fuel gets too low)
        if (telemetry.getFuelLevel() < 10 && random.nextDouble() < 0.1) { // 10% chance to refill when fuel < 10%
            telemetry.setFuelLevel(100.0);
            System.out.println("⛽ Refueled " + vehicle.getVehicleId() + " to 100%");
        }
        
        // Engine status - stop vehicle when fuel is 0%
        if (telemetry.getFuelLevel() <= 0) {
            telemetry.setEngineStatus("stopped");
            telemetry.setSpeed(0); // Stop the vehicle
        } else if (telemetry.getEngineStatus().equals("stopped") && telemetry.getFuelLevel() > 0 && random.nextDouble() < 0.3) {
            telemetry.setEngineStatus("running");
        }
        
        // Route progress (more frequent changes)
        if (random.nextDouble() < 0.2) { // Increased from 0.1 to 0.2 (20% chance)
            String newRoute = routes[random.nextInt(routes.length)];
            telemetry.setRoute(newRoute);
            vehicleRoutes.put(vehicle.getVehicleId(), newRoute);
        }
    }
    
    private void updateExpectedLocation(Vehicle vehicle) {
        // Simple expected location calculation based on route
        Location current = vehicle.getLocation();
        Location expected = expectedLocations.get(vehicle.getVehicleId());
        
        if (expected != null) {
            // Move expected location slightly towards destination
            double latDiff = (random.nextDouble() - 0.5) * 0.005;
            double lonDiff = (random.nextDouble() - 0.5) * 0.005;
            expected.setLat(expected.getLat() + latDiff);
            expected.setLon(expected.getLon() + lonDiff);
        }
    }
    
    private Integer getSeverityLevel(String severity) {
        switch (severity) {
            case "CRITICAL": return 4;
            case "HIGH": return 3;
            case "MEDIUM": return 2;
            case "LOW": return 1;
            default: return 0;
        }
    }
    
    private List<Alert> checkForAlerts(Vehicle vehicle) {
        List<Alert> alerts = new ArrayList<>();
        Telemetry telemetry = vehicle.getTelemetry();
        
        // Speed alerts (only if engine is running)
        if (telemetry.getEngineStatus().equals("running") && telemetry.getSpeed() > 80) {
            alerts.add(Alert.createOverspeedingAlert(vehicle.getVehicleId(), telemetry.getSpeed()));
        }
        
        // Fuel alerts (less aggressive threshold)
        if (telemetry.getFuelLevel() < 15) { // Changed from 25 to 15
            alerts.add(Alert.createLowFuelAlert(vehicle.getVehicleId(), telemetry.getFuelLevel()));
        }
        
        // Engine failure alerts (when stopped due to fuel or other issues)
        if (telemetry.getEngineStatus().equals("stopped")) {
            if (telemetry.getFuelLevel() <= 0) {
                alerts.add(Alert.createEngineFailureAlert(vehicle.getVehicleId()));
            }
        }
        
        // Route deviation alerts (simplified)
        if (random.nextDouble() < 0.02) { // Reduced from 0.05 to 0.02 (2% chance)
            alerts.add(Alert.createRouteDeviationAlert(vehicle.getVehicleId(), telemetry.getRoute()));
        }
        
        // Set the most critical alert as the main alert
        if (!alerts.isEmpty()) {
            Alert mostCritical = alerts.get(0);
            for (Alert alert : alerts) {
                if (getSeverityLevel(alert.getSeverity()) > getSeverityLevel(mostCritical.getSeverity())) {
                    mostCritical = alert;
                }
            }
            vehicle.setAlert(mostCritical);
            System.out.println("⚠️  " + mostCritical.getSeverity() + " Alert for " + vehicle.getVehicleId() + ": " + mostCritical.getMessage());
        } else {
            vehicle.setAlert(null);
        }
        
        return alerts;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }
    
    public Vehicle getVehicleById(String vehicleId) {
        return vehicles.stream()
            .filter(v -> v.getVehicleId().equals(vehicleId))
            .findFirst()
            .orElse(null);
    }
} 