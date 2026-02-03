package com.kavak.vehicle_maintenance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponseDTO {
    
    private UUID id;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer year;
    private Integer currentMileage;
}
