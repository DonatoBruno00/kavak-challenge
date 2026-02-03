package com.kavak.vehicle_maintenance.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleAvailabilityResponseDTO {
    
    @Schema(example = "ABC-1234")
    private String licensePlate;
    
    @Schema(example = "true", description = "true if vehicle is available (no PENDING or IN_PROGRESS maintenances)")
    private boolean available;
}
