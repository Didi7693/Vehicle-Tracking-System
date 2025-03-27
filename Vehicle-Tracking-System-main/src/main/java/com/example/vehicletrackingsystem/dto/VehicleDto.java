package com.example.vehicletrackingsystem.dto;

import com.example.vehicletrackingsystem.model.Vehicle;

public class VehicleDto {
    private String licensePlate;
    private String owner;
    private String currentLocation;

    public VehicleDto(Vehicle vehicle) {
        this.licensePlate = vehicle.getLicensePlate();
        this.owner = vehicle.getOwner();
        this.currentLocation = vehicle.getCurrentLocation();
    }

    // Getters
    public String getLicensePlate() {
        return licensePlate;
    }

    public String getOwner() {
        return owner;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }
}
