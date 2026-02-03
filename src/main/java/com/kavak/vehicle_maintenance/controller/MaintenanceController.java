package com.kavak.vehicle_maintenance.controller;

import com.kavak.vehicle_maintenance.dto.request.MaintenanceRequestDTO;
import com.kavak.vehicle_maintenance.dto.response.MaintenanceResponseDTO;
import com.kavak.vehicle_maintenance.service.MaintenanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles/{licensePlate}/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Maintenance management endpoints")
public class MaintenanceController {
    
    private final MaintenanceService maintenanceService;
    
    @PostMapping
    public ResponseEntity<MaintenanceResponseDTO> registerMaintenance(
            @PathVariable String licensePlate,
            @Valid @RequestBody MaintenanceRequestDTO requestDTO) {
        MaintenanceResponseDTO response = maintenanceService.registerMaintenance(licensePlate, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
