package com.example.vehicletrackingsystem.controller;

import com.example.vehicletrackingsystem.service.VehicleService;
import com.example.vehicletrackingsystem.dto.VehicleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Use javax validation if Jakarta validation is not available
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = {
    "http://localhost:5500",
    "http://127.0.0.1:5500",
    "http://localhost:3000", 
    "http://127.0.0.1:3000",
    "file://"
}, allowCredentials = "true")
public class VehicleController {
    
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);
    
    private final VehicleService vehicleService;

    // Constructor-based dependency injection
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerVehicle(
            @RequestParam @NotBlank(message = "License plate cannot be blank") 
            @Pattern(regexp = "^[A-Z0-9]{6,8}$", message = "Invalid license plate format") 
            String licensePlate,
            
            @RequestParam @NotBlank(message = "Owner name cannot be blank") 
            String owner,
            
            @RequestParam(required = false, defaultValue = "0,0") 
            String location) {
        try {
            logger.info("Registering vehicle: License Plate = {}, Owner = {}", licensePlate, owner);
            
            // Register vehicle
            String registrationResult = vehicleService.registerVehicle(licensePlate, owner);

            // Update location if provided and different from default
            if (!location.equals("0,0")) {
                String locationUpdateResult = vehicleService.updateVehicleLocation(licensePlate, location);
                logger.info("Vehicle location updated: {}", locationUpdateResult);
                return ResponseEntity.ok(registrationResult + " " + locationUpdateResult);
            }

            return ResponseEntity.ok(registrationResult);
        } catch (IllegalArgumentException e) {
            logger.error("Vehicle registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during vehicle registration", e);
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(
            @RequestParam @NotBlank(message = "License plate is required") 
            String licensePlate) {
        try {
            logger.info("Fetching transaction history for: {}", licensePlate);
            
            // If ALL is passed, return all vehicles
            if ("ALL".equalsIgnoreCase(licensePlate)) {
                List<VehicleDto> vehicles = vehicleService.getAllVehicles();
                logger.info("Retrieved {} vehicles", vehicles.size());
                return ResponseEntity.ok(vehicles);
            }

            // Otherwise, return transaction history for specific vehicle
            Object result = vehicleService.viewTransactionHistory(licensePlate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error fetching vehicle history", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle history not found");
        }
    }

    @PutMapping("/update-location")
    public ResponseEntity<String> updateLocation(
            @RequestParam @NotBlank(message = "License plate cannot be blank") 
            @Pattern(regexp = "^[A-Z0-9]{6,8}$", message = "Invalid license plate format") 
            String licensePlate,
            
            @RequestParam @NotBlank(message = "Location cannot be blank") 
            String newLocation) {
        try {
            logger.info("Updating location for vehicle: {}", licensePlate);
            String result = vehicleService.updateVehicleLocation(licensePlate, newLocation);
            logger.info("Location update result: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Location update failed", e);
            return ResponseEntity.badRequest().body("Location update failed: " + e.getMessage());
        }
    }
}