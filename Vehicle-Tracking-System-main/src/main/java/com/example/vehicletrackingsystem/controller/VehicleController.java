package com.example.vehicletrackingsystem.controller;

import com.example.vehicletrackingsystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/register")
    public String registerVehicle(@RequestParam String licensePlate, @RequestParam String owner) {
        return vehicleService.registerVehicle(licensePlate, owner);
    }

    @PutMapping("/update-location")
    public String updateLocation(@RequestParam String licensePlate, @RequestParam String newLocation) {
        return vehicleService.updateVehicleLocation(licensePlate, newLocation);
    }

    @GetMapping("/history")
    public Object getTransactionHistory(@RequestParam String licensePlate) {
        return vehicleService.viewTransactionHistory(licensePlate);
    }
}
