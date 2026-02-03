package com.kavak.vehicle_maintenance.controller;

import com.kavak.vehicle_maintenance.dto.request.ChangeStatusRequestDTO;
import com.kavak.vehicle_maintenance.dto.response.MaintenanceResponseDTO;
import com.kavak.vehicle_maintenance.service.MaintenanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Maintenance management endpoints")
public class MaintenanceStatusController {
    
    private final MaintenanceService maintenanceService;
    
    @PatchMapping("/{maintenanceId}/status")
    public ResponseEntity<MaintenanceResponseDTO> changeStatus(
            @PathVariable Long maintenanceId,
            @Valid @RequestBody ChangeStatusRequestDTO requestDTO) {
        MaintenanceResponseDTO response = maintenanceService.changeStatus(maintenanceId, requestDTO);
        return ResponseEntity.ok(response);
    }
}
