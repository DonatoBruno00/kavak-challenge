package com.kavak.vehicle_maintenance.dto.response;

import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResponseDTO {
    
    @Schema(example = "1")
    private Long id;
    
    @Schema(example = "1")
    private Long vehicleId;
    
    private MaintenanceType type;
    private String description;
    private LocalDateTime creationDate;
    private MaintenanceStatus status;
    private BigDecimal estimatedCost;
    private BigDecimal finalCost;
}
