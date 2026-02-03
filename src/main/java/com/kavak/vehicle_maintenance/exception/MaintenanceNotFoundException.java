package com.kavak.vehicle_maintenance.exception;

public class MaintenanceNotFoundException extends DomainException {
    
    public MaintenanceNotFoundException(Long id) {
        super("Maintenance with id '" + id + "' not found");
    }
}
