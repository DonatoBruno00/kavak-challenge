package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Use case for retrieving a vehicle by its license plate.
 */
@Component
@RequiredArgsConstructor
public class GetVehicleUseCase {
    
    private final VehicleRepository vehicleRepository;
    
    /**
     * Retrieves a vehicle by its license plate.
     * 
     * @param licensePlate the vehicle's license plate
     * @return the vehicle
     * @throws VehicleNotFoundException if no vehicle found with given license plate
     */
    public Vehicle execute(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));
    }
}
