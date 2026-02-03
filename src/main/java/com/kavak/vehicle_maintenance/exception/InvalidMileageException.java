package com.kavak.vehicle_maintenance.exception;

/**
 * Exception thrown when attempting to set a mileage value
 * that is less than or equal to the current mileage.
 */
public class InvalidMileageException extends DomainException {
    
    public InvalidMileageException(String licensePlate, Integer currentMileage, Integer newMileage) {
        super("Cannot update mileage for vehicle '" + licensePlate + 
              "': new mileage (" + newMileage + ") must be greater than current (" + currentMileage + ")");
    }
}
