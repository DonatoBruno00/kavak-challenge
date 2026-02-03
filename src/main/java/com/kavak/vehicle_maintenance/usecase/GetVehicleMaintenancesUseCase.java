package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Use case for retrieving all maintenances of a vehicle.
 */
@Component
@RequiredArgsConstructor
public class GetVehicleMaintenancesUseCase {
    
    private final VehicleRepository vehicleRepository;
    
    /**
     * Retrieves all maintenances for a vehicle identified by license plate.
     * 
     * @param licensePlate the vehicle license plate
     * @return list of all maintenances for the vehicle
     * @throws VehicleNotFoundException if vehicle not found
     */
    public List<Maintenance> execute(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));
        
        return vehicle.getMaintenances();
    }
}
