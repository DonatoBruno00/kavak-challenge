package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.exception.DuplicateLicensePlateException;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case: Register a new vehicle in the system.
 * 
 * Business Rules:
 * - License plate must be unique across all vehicles
 * - All vehicle data must be valid (validated at DTO level)
 * 
 * @throws DuplicateLicensePlateException if license plate already exists
 */
@Component
@RequiredArgsConstructor
public class RegisterVehicleUseCase {
    
    private final VehicleRepository vehicleRepository;
    
    @Transactional
    public Vehicle execute(Vehicle vehicle) {
        if (vehicleRepository.existsByLicensePlate(vehicle.getLicensePlate())) {
            throw new DuplicateLicensePlateException(vehicle.getLicensePlate());
        }
    
        return vehicleRepository.save(vehicle);
    }
}
