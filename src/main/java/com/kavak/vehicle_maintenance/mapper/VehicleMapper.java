package com.kavak.vehicle_maintenance.mapper;

import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.dto.request.VehicleRequestDTO;
import com.kavak.vehicle_maintenance.dto.response.VehicleResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {
    
    public Vehicle toEntity(VehicleRequestDTO requestDTO) {
        return Vehicle.builder()
                .licensePlate(requestDTO.getLicensePlate())
                .brand(requestDTO.getBrand())
                .model(requestDTO.getModel())
                .year(requestDTO.getYear())
                .currentMileage(requestDTO.getCurrentMileage())
                .build();
    }
    
    public VehicleResponseDTO toResponseDTO(Vehicle vehicle) {
        return VehicleResponseDTO.builder()
                .id(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .currentMileage(vehicle.getCurrentMileage())
                .build();
    }
}
