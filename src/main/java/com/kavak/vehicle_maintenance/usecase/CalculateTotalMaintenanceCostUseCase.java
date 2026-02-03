package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.dto.response.VehicleMaintenanceCostResponseDTO;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Use case for calculating total maintenance cost of a vehicle.
 * Only considers COMPLETED maintenances with finalCost.
 */
@Component
@RequiredArgsConstructor
public class CalculateTotalMaintenanceCostUseCase {
    
    private final VehicleRepository vehicleRepository;
    
    /**
     * Calculates total maintenance cost for a vehicle.
     * Business rule: Only COMPLETED maintenances with finalCost are included.
     * 
     * @param licensePlate the vehicle license plate
     * @return response with licensePlate and total cost
     * @throws VehicleNotFoundException if vehicle not found
     */
    public VehicleMaintenanceCostResponseDTO execute(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));
        
        BigDecimal totalCost = vehicle.getMaintenances().stream()
                .filter(maintenance -> maintenance.getStatus() == MaintenanceStatus.COMPLETED)
                .filter(maintenance -> maintenance.getFinalCost() != null)
                .map(maintenance -> maintenance.getFinalCost())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return VehicleMaintenanceCostResponseDTO.builder()
                .licensePlate(licensePlate)
                .totalCost(totalCost)
                .build();
    }
}
