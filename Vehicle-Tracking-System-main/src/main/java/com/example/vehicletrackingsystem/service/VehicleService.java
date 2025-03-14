package com.example.vehicletrackingsystem.service;


import com.example.vehicletrackingsystem.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VehicleService {
    private Map<String, Vehicle> vehicles = new HashMap<>();

    public String registerVehicle(String licensePlate, String owner) {
        if (!vehicles.containsKey(licensePlate)) {
            vehicles.put(licensePlate, new Vehicle(licensePlate, owner));
            return "Vehicle registered: " + licensePlate;
        } else {
            return "Vehicle already registered.";
        }
    }

    public String updateVehicleLocation(String licensePlate, String newLocation) {
        Vehicle vehicle = vehicles.get(licensePlate);
        if (vehicle != null) {
            vehicle.updateLocation(newLocation);
            return "Location updated for vehicle: " + licensePlate;
        } else {
            return "Vehicle not found.";
        }
    }

    public Object viewTransactionHistory(String licensePlate) {
        Vehicle vehicle = vehicles.get(licensePlate);
        if (vehicle != null) {
            return vehicle.getTransactionHistory();
        } else {
            return "Vehicle not found.";
        }
    }
}
