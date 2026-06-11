package com.smart_caresync_system.appointment_service.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

    private Long patientId;

    private String name;

    private String surname;

    private String email;

}
