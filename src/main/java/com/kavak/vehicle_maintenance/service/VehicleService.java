package com.kavak.vehicle_maintenance.service;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.dto.request.UpdateMileageRequestDTO;
import com.kavak.vehicle_maintenance.dto.request.VehicleRequestDTO;
import com.kavak.vehicle_maintenance.dto.response.MaintenanceResponseDTO;
import com.kavak.vehicle_maintenance.dto.response.VehicleAvailabilityResponseDTO;
import com.kavak.vehicle_maintenance.dto.response.VehicleMaintenanceCostResponseDTO;
import com.kavak.vehicle_maintenance.dto.response.VehicleResponseDTO;
import com.kavak.vehicle_maintenance.mapper.MaintenanceMapper;
import com.kavak.vehicle_maintenance.mapper.VehicleMapper;
import com.kavak.vehicle_maintenance.usecase.CalculateTotalMaintenanceCostUseCase;
import com.kavak.vehicle_maintenance.usecase.CheckVehicleAvailabilityUseCase;
import com.kavak.vehicle_maintenance.usecase.GetActiveMaintenancesUseCase;
import com.kavak.vehicle_maintenance.usecase.GetVehicleMaintenancesUseCase;
import com.kavak.vehicle_maintenance.usecase.GetVehicleUseCase;
import com.kavak.vehicle_maintenance.usecase.RegisterVehicleUseCase;
import com.kavak.vehicle_maintenance.usecase.UpdateVehicleMileageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private final GetVehicleMaintenancesUseCase getVehicleMaintenancesUseCase;
    private final GetActiveMaintenancesUseCase getActiveMaintenancesUseCase;
    private final CalculateTotalMaintenanceCostUseCase calculateTotalMaintenanceCostUseCase;
    private final CheckVehicleAvailabilityUseCase checkVehicleAvailabilityUseCase;
    private final VehicleMapper vehicleMapper;
    private final MaintenanceMapper maintenanceMapper;
    
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
    
    public List<MaintenanceResponseDTO> getVehicleMaintenances(String licensePlate) {
        List<Maintenance> maintenances = getVehicleMaintenancesUseCase.execute(licensePlate);
        return maintenances.stream()
                .map(maintenanceMapper::toResponseDTO)
                .toList();
    }
    
    public List<MaintenanceResponseDTO> getActiveMaintenances(String licensePlate) {
        List<Maintenance> activeMaintenances = getActiveMaintenancesUseCase.execute(licensePlate);
        return activeMaintenances.stream()
                .map(maintenanceMapper::toResponseDTO)
                .toList();
    }
    
    public VehicleMaintenanceCostResponseDTO calculateTotalMaintenanceCost(String licensePlate) {
        return calculateTotalMaintenanceCostUseCase.execute(licensePlate);
    }
    
    public VehicleAvailabilityResponseDTO checkAvailability(String licensePlate) {
        return checkVehicleAvailabilityUseCase.execute(licensePlate);
    }
}
