package com.smart_caresync_system.appointment_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorResponse {

    private Long doctorId;

    private String name;

    private String surname;

    private String ssn;

    private String email;

    private String specialization;

    private String city;

    private String address;
}
