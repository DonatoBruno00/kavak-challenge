package com.kavak.vehicle_maintenance.exception;

public class VehicleNotFoundException extends DomainException {
    
    public VehicleNotFoundException(Long id) {
        super("Vehicle with id '" + id + "' not found");
    }
    
    public VehicleNotFoundException(String licensePlate) {
        super("Vehicle with license plate '" + licensePlate + "' not found");
    }
}
