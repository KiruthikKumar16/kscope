package com.kfleetguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    private String vehicleId;
    private Location location;
    private Telemetry telemetry;
    private Alert alert;
} 