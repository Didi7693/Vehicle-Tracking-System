package com.example.vehicletrackingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private String licensePlate;
    private String owner;
    private List<String> transactionHistory;
    private String currentLocation;

    public Vehicle(String licensePlate, String owner) {
        this.licensePlate = licensePlate;
        this.owner = owner;
        this.transactionHistory = new ArrayList<>();
        this.currentLocation = "Unknown";
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

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    private void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }
}