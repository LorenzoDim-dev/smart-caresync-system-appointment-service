package com.smart_caresync_system.appointment_service.service;

import com.smart_caresync_system.appointment_service.dto.request.AppointmentRequest;
import com.smart_caresync_system.appointment_service.dto.response.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse scheduleAppointment(AppointmentRequest appointmentRequest);

    List<AppointmentResponse> retrieveAppointmentsOfDoctor(Long doctorId);

    List<AppointmentResponse> retrieveAppointmentsOfPatient(Long patientId);

    void approveAppointment( Long appointmentId);

    void rejectAppointment(Long appointmentId);

    void markAsVisitedAppointment(Long appointmentId);
}
