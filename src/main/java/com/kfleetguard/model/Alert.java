package com.kfleetguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alert {
    private String vehicleId;
    private String type;
    private String message;
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    private String timestamp; // Changed from LocalDateTime to String for Firebase compatibility
    private boolean acknowledged;
    
    public Alert(String vehicleId, String type, String message, String severity, LocalDateTime timestamp) {
        this.vehicleId = vehicleId;
        this.type = type;
        this.message = message;
        this.severity = severity;
        this.timestamp = timestamp.toString(); // Convert to String
        this.acknowledged = false;
    }
    
    public static Alert createOverspeedingAlert(String vehicleId, double speed) {
        return new Alert(vehicleId, "OVERSPEEDING", 
            "Vehicle speed: " + String.format("%.1f", speed) + " km/h", 
            speed > 100 ? "CRITICAL" : "HIGH", LocalDateTime.now());
    }
    
    public static Alert createLowFuelAlert(String vehicleId, double fuelLevel) {
        return new Alert(vehicleId, "LOW_FUEL", 
            "Fuel level: " + String.format("%.1f", fuelLevel) + "%", 
            fuelLevel < 10 ? "CRITICAL" : fuelLevel < 20 ? "HIGH" : "MEDIUM", LocalDateTime.now());
    }
    
    public static Alert createEngineFailureAlert(String vehicleId) {
        return new Alert(vehicleId, "ENGINE_FAILURE", 
            "Engine has stopped unexpectedly", "CRITICAL", LocalDateTime.now());
    }
    
    public static Alert createRouteDeviationAlert(String vehicleId, String expectedRoute) {
        return new Alert(vehicleId, "ROUTE_DEVIATION", 
            "Vehicle deviated from route: " + expectedRoute, "MEDIUM", LocalDateTime.now());
    }
} 