package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Use case for retrieving only active maintenances of a vehicle.
 * Active maintenances are those with status PENDING or IN_PROGRESS.
 */
@Component
@RequiredArgsConstructor
public class GetActiveMaintenancesUseCase {
    
    private final VehicleRepository vehicleRepository;
    
    /**
     * Retrieves only active maintenances (PENDING or IN_PROGRESS) for a vehicle.
     * 
     * @param licensePlate the vehicle license plate
     * @return list of active maintenances
     * @throws VehicleNotFoundException if vehicle not found
     */
    public List<Maintenance> execute(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));
        
        return vehicle.getMaintenances().stream()
                .filter(maintenance -> 
                    maintenance.getStatus() == MaintenanceStatus.PENDING || 
                    maintenance.getStatus() == MaintenanceStatus.IN_PROGRESS
                )
                .toList();
    }
}
