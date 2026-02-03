package com.kavak.vehicle_maintenance.service;

import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.dto.request.UpdateMileageRequestDTO;
import com.kavak.vehicle_maintenance.dto.request.VehicleRequestDTO;
import com.kavak.vehicle_maintenance.dto.response.VehicleResponseDTO;
import com.kavak.vehicle_maintenance.mapper.VehicleMapper;
import com.kavak.vehicle_maintenance.usecase.GetVehicleUseCase;
import com.kavak.vehicle_maintenance.usecase.RegisterVehicleUseCase;
import com.kavak.vehicle_maintenance.usecase.UpdateVehicleMileageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service layer for Vehicle operations.
 * Orchestrates between controllers and use cases.
 */
@Service
@RequiredArgsConstructor
public class VehicleService {
    
    private final RegisterVehicleUseCase registerVehicleUseCase;
    private final UpdateVehicleMileageUseCase updateVehicleMileageUseCase;
    private final GetVehicleUseCase getVehicleUseCase;
    private final VehicleMapper vehicleMapper;
    
    public VehicleResponseDTO registerVehicle(VehicleRequestDTO requestDTO) {
        Vehicle vehicle = vehicleMapper.toEntity(requestDTO);
        Vehicle savedVehicle = registerVehicleUseCase.execute(vehicle);
        return vehicleMapper.toResponseDTO(savedVehicle);
    }
    
    public VehicleResponseDTO updateMileage(String licensePlate, UpdateMileageRequestDTO requestDTO) {
        Vehicle updatedVehicle = updateVehicleMileageUseCase.execute(licensePlate, requestDTO.getCurrentMileage());
        return vehicleMapper.toResponseDTO(updatedVehicle);
    }
    
    public VehicleResponseDTO getVehicle(String licensePlate) {
        Vehicle vehicle = getVehicleUseCase.execute(licensePlate);
        return vehicleMapper.toResponseDTO(vehicle);
    }
}
