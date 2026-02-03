package com.kavak.vehicle_maintenance.dto.response;

import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResponseDTO {
    
    private UUID id;
    private UUID vehicleId;
    private MaintenanceType type;
    private String description;
    private LocalDateTime creationDate;
    private MaintenanceStatus status;
    private BigDecimal estimatedCost;
    private BigDecimal finalCost;
}
