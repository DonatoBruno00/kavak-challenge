package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.MaintenanceRepository;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Use case for registering a new maintenance for a vehicle.
 * The maintenance is created with PENDING status.
 */
@Component
@RequiredArgsConstructor
public class RegisterMaintenanceUseCase {
    
    private final VehicleRepository vehicleRepository;
    private final MaintenanceRepository maintenanceRepository;
    
    /**
     * Registers a new maintenance for a vehicle.
     * 
     * @param licensePlate the vehicle's license plate
     * @param maintenance the maintenance to register (with status and creationDate already set)
     * @return the saved maintenance
     * @throws VehicleNotFoundException if no vehicle found with given license plate
     */
    public Maintenance execute(String licensePlate, Maintenance maintenance) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));
        
        maintenance.setVehicle(vehicle);
        return maintenanceRepository.save(maintenance);
    }
}
