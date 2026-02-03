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
public class VehicleResponseDTO {
    
    @Schema(example = "1")
    private Long id;
    
    private String licensePlate;
    private String brand;
    private String model;
    private Integer year;
    private Integer currentMileage;
}
