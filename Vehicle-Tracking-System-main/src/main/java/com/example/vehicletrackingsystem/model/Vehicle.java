package com.example.vehicletrackingsystem.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private String licensePlate;
    private String owner;
    private List<VehicleTransaction> transactionHistory;
    private String currentLocation;
    private LocalDateTime registrationTime;

    public Vehicle(String licensePlate, String owner) {
        this.licensePlate = licensePlate;
        this.owner = owner;
        this.transactionHistory = new ArrayList<>();
        this.currentLocation = "0,0";
        this.registrationTime = LocalDateTime.now();
        addTransaction("Vehicle Registered");
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getOwner() {
        return owner;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void updateLocation(String newLocation) {
        this.currentLocation = newLocation;
        addTransaction("Location updated to: " + newLocation);
    }

    public List<VehicleTransaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    private void addTransaction(String description) {
        transactionHistory.add(new VehicleTransaction(description));
    }

    // Inner class for tracking transactions
    public static class VehicleTransaction {
        private LocalDateTime timestamp;
        private String description;

        public VehicleTransaction(String description) {
            this.timestamp = LocalDateTime.now();
            this.description = description;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getDescription() {
            return description;
        }
    }

    // Serialization helper methods
    public String toJsonString() {
        return String.format(
            "{\"licensePlate\":\"%s\",\"owner\":\"%s\",\"currentLocation\":\"%s\"}",
            licensePlate, owner, currentLocation
        );
    }
}
