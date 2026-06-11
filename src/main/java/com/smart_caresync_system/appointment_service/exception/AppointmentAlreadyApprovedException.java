package com.smart_caresync_system.appointment_service.exception;

public class AppointmentAlreadyApprovedException extends RuntimeException {
    public AppointmentAlreadyApprovedException(String message) {
        super(message);
    }
}
