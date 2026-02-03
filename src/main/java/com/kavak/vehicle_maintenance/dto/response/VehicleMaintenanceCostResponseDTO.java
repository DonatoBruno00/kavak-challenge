package com.kavak.vehicle_maintenance.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleMaintenanceCostResponseDTO {
    
    @Schema(example = "ABC-1234")
    private String licensePlate;
    
    @Schema(example = "1250.50", description = "Total cost of completed maintenances only")
    private BigDecimal totalCost;
}
