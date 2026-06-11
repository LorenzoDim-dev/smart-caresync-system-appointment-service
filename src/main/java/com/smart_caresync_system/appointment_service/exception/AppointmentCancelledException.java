package com.smart_caresync_system.appointment_service.exception;

public class AppointmentCancelledException extends RuntimeException {
    public AppointmentCancelledException(String message) {
        super(message);
    }
}
