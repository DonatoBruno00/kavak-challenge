package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.dto.response.VehicleAvailabilityResponseDTO;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Use case for checking if a vehicle is available.
 * A vehicle is NOT available if it has any maintenance with status PENDING or IN_PROGRESS.
 */
@Component
@RequiredArgsConstructor
public class CheckVehicleAvailabilityUseCase {
    
    private final VehicleRepository vehicleRepository;
    
    /**
     * Checks if a vehicle is available for use.
     * 
     * @param licensePlate the vehicle license plate
     * @return availability response with status
     * @throws VehicleNotFoundException if vehicle not found
     */
    public VehicleAvailabilityResponseDTO execute(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));
        
        boolean available = vehicle.getMaintenances().stream()
                .noneMatch(maintenance -> 
                    maintenance.getStatus() == MaintenanceStatus.PENDING || 
                    maintenance.getStatus() == MaintenanceStatus.IN_PROGRESS
                );
        
        return VehicleAvailabilityResponseDTO.builder()
                .licensePlate(licensePlate)
                .available(available)
                .build();
    }
}
