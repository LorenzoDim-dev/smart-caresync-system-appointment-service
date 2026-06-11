package com.smart_caresync_system.appointment_service.controller;

import com.smart_caresync_system.appointment_service.dto.request.AppointmentRequest;
import com.smart_caresync_system.appointment_service.dto.response.AppointmentResponse;
import com.smart_caresync_system.appointment_service.model.Appointment;
import com.smart_caresync_system.appointment_service.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppointmentControllerImpl implements AppointmentController{

    private  final AppointmentService appointmentService;


    @Override
    public ResponseEntity<AppointmentResponse> scheduleAppointment(AppointmentRequest request) {
        AppointmentResponse appointmentResponse = appointmentService.scheduleAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentResponse);
    }

    @Override
    public ResponseEntity<List<AppointmentResponse>> retrieveAppointmentsOfDoctor(Long doctorId) {
        List<AppointmentResponse> appointmentResponseList = appointmentService.retrieveAppointmentsOfDoctor(doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentResponseList);
    }

    @Override
    public ResponseEntity<List<AppointmentResponse>> retrieveAppointmentsOfPatient(Long patientId) {
        List<AppointmentResponse> appointmentResponseList = appointmentService.retrieveAppointmentsOfPatient(patientId);
        return  ResponseEntity.status(HttpStatus.OK).body(appointmentResponseList);
    }

    @Override
    public ResponseEntity<Void> approveAppointment(Long appointmentId) {
        appointmentService.approveAppointment(appointmentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Void> rejectAppointment(Long appointmentId) {
        appointmentService.rejectAppointment(appointmentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Void> markAsVisitedAppointment(Long appointmentId) {
        appointmentService.markAsVisitedAppointment(appointmentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
