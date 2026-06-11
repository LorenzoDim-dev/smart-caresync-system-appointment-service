package com.smart_caresync_system.appointment_service.dto.request;

import com.smart_caresync_system.appointment_service.constant.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotNull(message = "{patient.required}")
    private Long patientId;

    @NotNull(message = "{doctor.required}")
    private Long doctorId;

    @NotNull(message = "{date.required}")
    private LocalDateTime appointmentDateTime;

}
