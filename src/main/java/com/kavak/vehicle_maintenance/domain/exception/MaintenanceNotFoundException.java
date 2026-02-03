package com.kavak.vehicle_maintenance.domain.exception;

import java.util.UUID;

public class MaintenanceNotFoundException extends DomainException {
    
    public MaintenanceNotFoundException(UUID id) {
        super(String.format("Maintenance with id '%s' not found", id));
    }
}
