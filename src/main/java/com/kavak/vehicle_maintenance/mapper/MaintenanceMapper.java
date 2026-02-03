package com.kavak.vehicle_maintenance.mapper;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.dto.request.MaintenanceRequestDTO;
import com.kavak.vehicle_maintenance.dto.response.MaintenanceResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MaintenanceMapper {
    
    /**
     * Converts a request DTO to a Maintenance entity.
     * Sets initial status to PENDING and creationDate to now.
     * Vehicle is set later by the use case.
     */
    public Maintenance toEntity(MaintenanceRequestDTO requestDTO) {
        return Maintenance.builder()
                .type(requestDTO.getType())
                .description(requestDTO.getDescription())
                .estimatedCost(requestDTO.getEstimatedCost())
                .creationDate(LocalDateTime.now())
                .status(MaintenanceStatus.PENDING)
                .build();
    }
    
    public MaintenanceResponseDTO toResponseDTO(Maintenance maintenance) {
        return MaintenanceResponseDTO.builder()
                .id(maintenance.getId())
                .vehicleId(maintenance.getVehicle().getId())
                .type(maintenance.getType())
                .description(maintenance.getDescription())
                .creationDate(maintenance.getCreationDate())
                .status(maintenance.getStatus())
                .estimatedCost(maintenance.getEstimatedCost())
                .finalCost(maintenance.getFinalCost())
                .build();
    }
}
