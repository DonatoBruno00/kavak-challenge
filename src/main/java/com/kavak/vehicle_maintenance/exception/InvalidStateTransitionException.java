package com.kavak.vehicle_maintenance.exception;

public class InvalidStateTransitionException extends DomainException {
       
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}
