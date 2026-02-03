package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.exception.InvalidStateTransitionException;
import com.kavak.vehicle_maintenance.exception.MaintenanceNotFoundException;
import com.kavak.vehicle_maintenance.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * Use case for changing the status of a maintenance.
 * Validates state transitions according to business rules.
 */
@Component
@RequiredArgsConstructor
public class ChangeMaintenanceStatusUseCase {
    
    private final MaintenanceRepository maintenanceRepository;
    
    private static final Map<MaintenanceStatus, Set<MaintenanceStatus>> VALID_TRANSITIONS = Map.of(
        MaintenanceStatus.PENDING, Set.of(MaintenanceStatus.IN_PROGRESS, MaintenanceStatus.CANCELLED),
        MaintenanceStatus.IN_PROGRESS, Set.of(MaintenanceStatus.COMPLETED, MaintenanceStatus.CANCELLED),
        MaintenanceStatus.COMPLETED, Set.of(),
        MaintenanceStatus.CANCELLED, Set.of()
    );
    
    /**
     * Changes the status of a maintenance.
     * 
     * @param maintenanceId the maintenance ID
     * @param newStatus the new status
     * @param finalCost the final cost (required only when completing)
     * @return the updated maintenance
     * @throws MaintenanceNotFoundException if maintenance not found
     * @throws InvalidStateTransitionException if transition is not valid
     */
    public Maintenance execute(Long maintenanceId, MaintenanceStatus newStatus, BigDecimal finalCost) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new MaintenanceNotFoundException(maintenanceId));
        
        MaintenanceStatus currentStatus = maintenance.getStatus();
        Set<MaintenanceStatus> validNextStates = VALID_TRANSITIONS.get(currentStatus);
        
        if (!validNextStates.contains(newStatus)) {
            throw new InvalidStateTransitionException("Invalid state transition from " + currentStatus + " to " + newStatus);
        }
        
        if (newStatus == MaintenanceStatus.COMPLETED) {
            if (finalCost == null) {
                throw new InvalidStateTransitionException("Final cost is required when completing a maintenance");
            }
            maintenance.setFinalCost(finalCost);
        }
        
        maintenance.setStatus(newStatus);
        return maintenanceRepository.save(maintenance);
    }
}
