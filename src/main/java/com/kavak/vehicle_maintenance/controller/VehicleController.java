package com.kavak.vehicle_maintenance.controller;

import com.kavak.vehicle_maintenance.dto.request.VehicleRequestDTO;
import com.kavak.vehicle_maintenance.dto.response.VehicleResponseDTO;
import com.kavak.vehicle_maintenance.service.VehicleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
