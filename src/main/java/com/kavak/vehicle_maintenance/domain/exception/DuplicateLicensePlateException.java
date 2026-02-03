package com.kavak.vehicle_maintenance.domain.exception;

public class DuplicateLicensePlateException extends DomainException {
    
    public DuplicateLicensePlateException(String licensePlate) {
        super(String.format("Vehicle with license plate '%s' already exists", licensePlate));
    }
}
