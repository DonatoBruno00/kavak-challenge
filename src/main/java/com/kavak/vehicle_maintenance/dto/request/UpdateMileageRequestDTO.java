package com.kavak.vehicle_maintenance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMileageRequestDTO {
    
    @Schema(description = "New mileage value in kilometers", example = "18500")
    @NotNull(message = "Mileage is required")
    @Min(value = 0, message = "Mileage must be greater than or equal to 0")
    private Integer currentMileage;
}
