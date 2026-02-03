package com.kavak.vehicle_maintenance.dto.request;

import com.kavak.vehicle_maintenance.domain.enums.MaintenanceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequestDTO {
    
    @Schema(description = "Type of maintenance", example = "OIL_CHANGE")
    @NotNull(message = "Maintenance type is required")
    private MaintenanceType type;
    
    @Schema(description = "Description of the maintenance work", example = "Regular oil change and filter replacement")
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @Schema(description = "Estimated cost in dollars", example = "150.00")
    @NotNull(message = "Estimated cost is required")
    @Positive(message = "Estimated cost must be positive")
    private BigDecimal estimatedCost;
}
