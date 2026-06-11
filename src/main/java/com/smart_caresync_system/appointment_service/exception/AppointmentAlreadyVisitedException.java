package com.smart_caresync_system.appointment_service.exception;

public class AppointmentAlreadyVisitedException extends RuntimeException {
    public AppointmentAlreadyVisitedException(String message) {
        super(message);
    }
}
