package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.exception.InvalidMileageException;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Use case for updating a vehicle's mileage.
 * Business rule: new mileage must be greater than current mileage.
 */
@Component
@RequiredArgsConstructor
public class UpdateVehicleMileageUseCase {
    
    private final VehicleRepository vehicleRepository;
    
    /**
     * Updates the mileage of a vehicle identified by its license plate.
     * 
     * @param licensePlate the vehicle's license plate
     * @param newMileage the new mileage value
     * @return the updated vehicle
     * @throws VehicleNotFoundException if no vehicle found with given license plate
     * @throws InvalidMileageException if new mileage is not greater than current
     */
    public Vehicle execute(String licensePlate, Integer newMileage) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));
        
        if (newMileage <= vehicle.getCurrentMileage()) {
            throw new InvalidMileageException(licensePlate, vehicle.getCurrentMileage(), newMileage);
        }
        
        vehicle.setCurrentMileage(newMileage);
        return vehicleRepository.save(vehicle);
    }
}
