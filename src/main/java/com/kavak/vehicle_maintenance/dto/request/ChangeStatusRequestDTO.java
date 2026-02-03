package com.kavak.vehicle_maintenance.dto.request;

import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusRequestDTO {
    
    @Schema(description = "New status for the maintenance", example = "IN_PROGRESS")
    @NotNull(message = "New status is required")
    private MaintenanceStatus newStatus;
    
    @Schema(description = "Final cost (required only when completing)", example = "175.50")
    @Positive(message = "Final cost must be positive")
    private BigDecimal finalCost;
}
