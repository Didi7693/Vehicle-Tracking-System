package com.example.vehicletrackingsystem.service;

import com.example.vehicletrackingsystem.model.Vehicle;
import com.example.vehicletrackingsystem.dto.VehicleDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    private Map<String, Vehicle> vehicles = new HashMap<>();

    public String registerVehicle(String licensePlate, String owner) {
        // Validate input
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be empty");
        }

        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be empty");
        }

        // Check for existing vehicle
        if (vehicles.containsKey(licensePlate.toUpperCase())) {
            return "Vehicle already registered.";
        }

        // Create and store vehicle
        Vehicle vehicle = new Vehicle(licensePlate.toUpperCase(), owner);
        vehicles.put(licensePlate.toUpperCase(), vehicle);
        return "Vehicle registered: " + licensePlate;
    }

    public String updateVehicleLocation(String licensePlate, String newLocation) {
        // Validate input
        Vehicle vehicle = vehicles.get(licensePlate.toUpperCase());
        if (vehicle == null) {
            return "Vehicle not found.";
        }

        // Validate location format
        if (!isValidLocation(newLocation)) {
            return "Invalid location format. Use 'latitude,longitude'.";
        }

        vehicle.updateLocation(newLocation);
        return "Location updated for vehicle: " + licensePlate;
    }

    public List<VehicleDto> getAllVehicles() {
        return vehicles.values().stream()
            .map(VehicleDto::new)
            .collect(Collectors.toList());
    }

    public Object viewTransactionHistory(String licensePlate) {
        Vehicle vehicle = vehicles.get(licensePlate.toUpperCase());
        if (vehicle == null) {
            return "Vehicle not found.";
        }
        return vehicle.getTransactionHistory();
    }

    // Helper method to validate location
    private boolean isValidLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return false;
        }

        String[] coords = location.split(",");
        if (coords.length != 2) {
            return false;
        }

        try {
            double latitude = Double.parseDouble(coords[0]);
            double longitude = Double.parseDouble(coords[1]);

            return latitude >= -90 && latitude <= 90 &&
                   longitude >= -180 && longitude <= 180;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}