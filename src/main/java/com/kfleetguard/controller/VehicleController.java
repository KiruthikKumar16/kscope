package com.kfleetguard.controller;

import com.kfleetguard.model.Vehicle;
import com.kfleetguard.service.AlertService;
import com.kfleetguard.service.VehicleSimulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {
    @Autowired
    private VehicleSimulatorService simulatorService;
    @Autowired
    private AlertService alertService;

    @GetMapping("")
    public List<Vehicle> getAllVehicles() {
        return simulatorService.getVehicles();
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String vehicleId) {
        Vehicle vehicle = simulatorService.getVehicleById(vehicleId);
        if (vehicle != null) {
            return ResponseEntity.ok(vehicle);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{vehicleId}/alerts")
    public Object getAlerts(@PathVariable String vehicleId) {
        return alertService.getAlertsForVehicle(vehicleId);
    }

    @GetMapping("/analytics/summary")
    public Map<String, Object> getAnalyticsSummary() {
        List<Vehicle> vehicles = simulatorService.getVehicles();
        Map<String, Object> summary = new HashMap<>();
        
        // Calculate statistics
        long totalVehicles = vehicles.size();
        long runningVehicles = vehicles.stream()
            .filter(v -> "running".equals(v.getTelemetry().getEngineStatus()))
            .count();
        long vehiclesWithAlerts = vehicles.stream()
            .filter(v -> v.getAlert() != null)
            .count();
        
        double avgSpeed = vehicles.stream()
            .mapToDouble(v -> v.getTelemetry().getSpeed())
            .average()
            .orElse(0.0);
        
        double avgFuelLevel = vehicles.stream()
            .mapToDouble(v -> v.getTelemetry().getFuelLevel())
            .average()
            .orElse(0.0);
        
        summary.put("totalVehicles", totalVehicles);
        summary.put("runningVehicles", runningVehicles);
        summary.put("vehiclesWithAlerts", vehiclesWithAlerts);
        summary.put("averageSpeed", Math.round(avgSpeed * 100.0) / 100.0);
        summary.put("averageFuelLevel", Math.round(avgFuelLevel * 100.0) / 100.0);
        summary.put("operationalRate", Math.round((double) runningVehicles / totalVehicles * 100));
        
        return summary;
    }

    @GetMapping("/analytics/alerts")
    public Map<String, Object> getAlertAnalytics() {
        List<Vehicle> vehicles = simulatorService.getVehicles();
        Map<String, Object> analytics = new HashMap<>();
        
        // Count alerts by type
        long overspeedingAlerts = vehicles.stream()
            .filter(v -> v.getAlert() != null && "OVERSPEEDING".equals(v.getAlert().getType()))
            .count();
        
        long lowFuelAlerts = vehicles.stream()
            .filter(v -> v.getAlert() != null && "LOW_FUEL".equals(v.getAlert().getType()))
            .count();
        
        long engineFailureAlerts = vehicles.stream()
            .filter(v -> v.getAlert() != null && "ENGINE_FAILURE".equals(v.getAlert().getType()))
            .count();
        
        long routeDeviationAlerts = vehicles.stream()
            .filter(v -> v.getAlert() != null && "ROUTE_DEVIATION".equals(v.getAlert().getType()))
            .count();
        
        analytics.put("overspeedingAlerts", overspeedingAlerts);
        analytics.put("lowFuelAlerts", lowFuelAlerts);
        analytics.put("engineFailureAlerts", engineFailureAlerts);
        analytics.put("routeDeviationAlerts", routeDeviationAlerts);
        analytics.put("totalAlerts", overspeedingAlerts + lowFuelAlerts + engineFailureAlerts + routeDeviationAlerts);
        
        return analytics;
    }

    @GetMapping("/export/telemetry")
    public ResponseEntity<byte[]> exportTelemetryCSV() {
        List<Vehicle> vehicles = simulatorService.getVehicles();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);
        
        // CSV Header
        writer.println("Vehicle ID,Timestamp,Latitude,Longitude,Speed (km/h),Fuel Level (%),Engine Status,Route,Alert Type,Alert Message,Alert Severity");
        
        // CSV Data
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        for (Vehicle vehicle : vehicles) {
            String alertType = vehicle.getAlert() != null ? vehicle.getAlert().getType() : "";
            String alertMessage = vehicle.getAlert() != null ? vehicle.getAlert().getMessage() : "";
            String alertSeverity = vehicle.getAlert() != null ? vehicle.getAlert().getSeverity() : "";
            
            writer.printf("\"%s\",\"%s\",%.6f,%.6f,%.1f,%.1f,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                vehicle.getVehicleId(),
                timestamp,
                vehicle.getLocation().getLat(),
                vehicle.getLocation().getLon(),
                vehicle.getTelemetry().getSpeed(),
                vehicle.getTelemetry().getFuelLevel(),
                vehicle.getTelemetry().getEngineStatus(),
                vehicle.getTelemetry().getRoute(),
                alertType,
                alertMessage,
                alertSeverity
            );
        }
        
        writer.close();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "fleet_telemetry_" + timestamp.replace(":", "-") + ".csv");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(baos.toByteArray());
    }

    @GetMapping("/export/analytics")
    public ResponseEntity<byte[]> exportAnalyticsCSV() {
        List<Vehicle> vehicles = simulatorService.getVehicles();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);
        
        // Calculate analytics
        long totalVehicles = vehicles.size();
        long runningVehicles = vehicles.stream()
            .filter(v -> "running".equals(v.getTelemetry().getEngineStatus()))
            .count();
        long stoppedVehicles = totalVehicles - runningVehicles;
        
        double avgSpeed = vehicles.stream()
            .mapToDouble(v -> v.getTelemetry().getSpeed())
            .average()
            .orElse(0.0);
        
        double avgFuelLevel = vehicles.stream()
            .mapToDouble(v -> v.getTelemetry().getFuelLevel())
            .average()
            .orElse(0.0);
        
        long overspeedingAlerts = vehicles.stream()
            .filter(v -> v.getAlert() != null && "OVERSPEEDING".equals(v.getAlert().getType()))
            .count();
        
        long lowFuelAlerts = vehicles.stream()
            .filter(v -> v.getAlert() != null && "LOW_FUEL".equals(v.getAlert().getType()))
            .count();
        
        long engineFailureAlerts = vehicles.stream()
            .filter(v -> v.getAlert() != null && "ENGINE_FAILURE".equals(v.getAlert().getType()))
            .count();
        
        long routeDeviationAlerts = vehicles.stream()
            .filter(v -> v.getAlert() != null && "ROUTE_DEVIATION".equals(v.getAlert().getType()))
            .count();
        
        // CSV Header
        writer.println("Metric,Value,Unit");
        
        // CSV Data
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        writer.printf("Export Timestamp,%s,%s%n", timestamp, "");
        writer.printf("Total Vehicles,%d,units%n", totalVehicles);
        writer.printf("Running Vehicles,%d,units%n", runningVehicles);
        writer.printf("Stopped Vehicles,%d,units%n", stoppedVehicles);
        writer.printf("Operational Rate,%.1f,%%%n", (double) runningVehicles / totalVehicles * 100);
        writer.printf("Average Speed,%.1f,km/h%n", avgSpeed);
        writer.printf("Average Fuel Level,%.1f,%%%n", avgFuelLevel);
        writer.printf("Overspeeding Alerts,%d,units%n", overspeedingAlerts);
        writer.printf("Low Fuel Alerts,%d,units%n", lowFuelAlerts);
        writer.printf("Engine Failure Alerts,%d,units%n", engineFailureAlerts);
        writer.printf("Route Deviation Alerts,%d,units%n", routeDeviationAlerts);
        writer.printf("Total Alerts,%d,units%n", overspeedingAlerts + lowFuelAlerts + engineFailureAlerts + routeDeviationAlerts);
        
        writer.close();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "fleet_analytics_" + timestamp.replace(":", "-") + ".csv");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(baos.toByteArray());
    }

    @GetMapping("/export/vehicle-details")
    public ResponseEntity<byte[]> exportVehicleDetailsCSV() {
        List<Vehicle> vehicles = simulatorService.getVehicles();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);
        
        // CSV Header
        writer.println("Vehicle ID,Current Status,Current Speed,Current Fuel,Current Route,Last Alert,Alert Severity,Location");
        
        // CSV Data
        for (Vehicle vehicle : vehicles) {
            String alertInfo = vehicle.getAlert() != null ? vehicle.getAlert().getMessage() : "No Alerts";
            String alertSeverity = vehicle.getAlert() != null ? vehicle.getAlert().getSeverity() : "N/A";
            String location = String.format("%.6f, %.6f", vehicle.getLocation().getLat(), vehicle.getLocation().getLon());
            
            writer.printf("\"%s\",\"%s\",%.1f,%.1f,\"%s\",\"%s\",\"%s\",\"%s\"%n",
                vehicle.getVehicleId(),
                vehicle.getTelemetry().getEngineStatus(),
                vehicle.getTelemetry().getSpeed(),
                vehicle.getTelemetry().getFuelLevel(),
                vehicle.getTelemetry().getRoute(),
                alertInfo,
                alertSeverity,
                location
            );
        }
        
        writer.close();
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "vehicle_details_" + timestamp.replace(":", "-") + ".csv");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(baos.toByteArray());
    }

    @GetMapping("/status")
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "OPERATIONAL");
        status.put("timestamp", System.currentTimeMillis());
        status.put("version", "1.0.0");
        status.put("firebaseConnected", true);
        return status;
    }
} 