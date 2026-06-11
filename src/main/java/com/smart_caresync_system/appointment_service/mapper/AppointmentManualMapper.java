package com.smart_caresync_system.appointment_service.mapper;

import com.smart_caresync_system.appointment_service.dto.response.AppointmentResponse;
import com.smart_caresync_system.appointment_service.dto.response.DoctorResponse;
import com.smart_caresync_system.appointment_service.dto.response.PatientResponse;
import com.smart_caresync_system.appointment_service.model.Appointment;
import com.smart_caresync_system.appointment_service.proxy.DoctorProxy;
import com.smart_caresync_system.appointment_service.proxy.PatientProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentManualMapper {

    private final PatientProxy patientProxy;
    private final DoctorProxy doctorProxy;

    public AppointmentResponse toResponse(Appointment appointment) {

        PatientResponse patient = patientProxy.retrievePatientDetails(appointment.getPatientId());

        DoctorResponse doctor = doctorProxy.retrieveDoctorDetails(appointment.getDoctorId());

        return AppointmentResponse.builder()
                .appointmentId(appointment.getAppointmentId())
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .status(appointment.getStatus())
                .build();
    }
}
