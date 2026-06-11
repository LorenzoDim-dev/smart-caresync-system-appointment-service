package com.smart_caresync_system.appointment_service.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestApiPath {
    public static final String API_V1 = "/api/v1";
    public static final String BASE_APPOINTMENT = API_V1 +  "/appointments";
    public static final String DOCTOR_APPOINTMENTS = "/doctor/{doctorId}";
    public static final String APPROVE_APPOINTMENT = "/approve/{appointmentId}";
    public static final String REJECT_APPOINTMENT = "/reject/{appointmentId}";
    public static final String MARK_AS_VISITED_APPOINTMENT = "/mark-as-visited/{appointmentId}";
    public static final String PATIENT_APPOINTMENTS = "/patient/{patientId}";
}
