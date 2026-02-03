package com.kavak.vehicle_maintenance.controller;

import com.kavak.vehicle_maintenance.dto.request.UpdateMileageRequestDTO;
import com.kavak.vehicle_maintenance.dto.request.VehicleRequestDTO;
import com.kavak.vehicle_maintenance.dto.response.MaintenanceResponseDTO;
import com.kavak.vehicle_maintenance.dto.response.VehicleResponseDTO;
import com.kavak.vehicle_maintenance.service.VehicleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Vehicle management endpoints")
public class VehicleController {
    
    private final VehicleService vehicleService;
    
    @PostMapping
    public ResponseEntity<VehicleResponseDTO> registerVehicle(
            @Valid @RequestBody VehicleRequestDTO requestDTO) {
        VehicleResponseDTO response = vehicleService.registerVehicle(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{licensePlate}")
    public ResponseEntity<VehicleResponseDTO> getVehicle(@PathVariable String licensePlate) {
        VehicleResponseDTO response = vehicleService.getVehicle(licensePlate);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{licensePlate}/mileage")
    public ResponseEntity<VehicleResponseDTO> updateMileage(
            @PathVariable String licensePlate,
            @Valid @RequestBody UpdateMileageRequestDTO requestDTO) {
        VehicleResponseDTO response = vehicleService.updateMileage(licensePlate, requestDTO);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{licensePlate}/maintenances")
    public ResponseEntity<List<MaintenanceResponseDTO>> getVehicleMaintenances(
            @PathVariable String licensePlate) {
        List<MaintenanceResponseDTO> response = vehicleService.getVehicleMaintenances(licensePlate);
        return ResponseEntity.ok(response);
    }
}
