package com.kavak.vehicle_maintenance.exception;

import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;

public class InvalidStateTransitionException extends DomainException {
    
    public InvalidStateTransitionException(MaintenanceStatus currentStatus, MaintenanceStatus newStatus) {
        super(String.format("Invalid state transition from %s to %s", currentStatus, newStatus));
    }
}
