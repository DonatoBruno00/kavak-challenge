package com.kavak.vehicle_maintenance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequestDTO {
    
    @Schema(description = "Vehicle license plate", example = "ABC-1234")
    @NotBlank(message = "License plate is required")
    @Size(max = 20, message = "License plate must not exceed 20 characters")
    @Pattern(
        regexp = "^[A-Z0-9]{2,4}-?[A-Z0-9]{2,4}$",
        message = "License plate format is invalid. Expected format: ABC-1234 or ABC1234"
    )
    private String licensePlate;
    
    @Schema(description = "Vehicle brand", example = "BMW")
    @NotBlank(message = "Brand is required")
    @Size(max = 50, message = "Brand must not exceed 50 characters")
    private String brand;
    
    @Schema(description = "Vehicle model", example = "135i")
    @NotBlank(message = "Model is required")
    @Size(max = 50, message = "Model must not exceed 50 characters")
    private String model;
    
    @Schema(description = "Manufacturing year", example = "2022")
    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be greater than or equal to 1900")
    @Max(value = 2026, message = "Year must be less than or equal to 2026")
    private Integer year;
    
    @Schema(description = "Current mileage in kilometers", example = "15000")
    @NotNull(message = "Current mileage is required")
    @Min(value = 0, message = "Current mileage must be greater than or equal to 0")
    private Integer currentMileage;
}
