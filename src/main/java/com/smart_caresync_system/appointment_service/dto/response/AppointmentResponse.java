package com.smart_caresync_system.appointment_service.dto.response;

import com.smart_caresync_system.appointment_service.constant.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long appointmentId;

    private PatientResponse patient;

    private DoctorResponse doctor;

    private LocalDateTime appointmentDateTime;

    private AppointmentStatus status;

}
