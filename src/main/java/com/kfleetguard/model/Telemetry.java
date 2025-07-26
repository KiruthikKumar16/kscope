package com.kfleetguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Telemetry {
    private double speed;
    private double fuelLevel;
    private String engineStatus;
    private String route;
} 