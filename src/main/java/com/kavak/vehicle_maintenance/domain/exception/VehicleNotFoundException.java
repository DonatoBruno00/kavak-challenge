package com.kavak.vehicle_maintenance.domain.exception;

import java.util.UUID;

public class VehicleNotFoundException extends DomainException {
    
    public VehicleNotFoundException(UUID id) {
        super(String.format("Vehicle with id '%s' not found", id));
    }
    
    public VehicleNotFoundException(String licensePlate) {
        super(String.format("Vehicle with license plate '%s' not found", licensePlate));
    }
}
