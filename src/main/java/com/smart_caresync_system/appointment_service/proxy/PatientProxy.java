package com.smart_caresync_system.appointment_service.proxy;

import com.smart_caresync_system.appointment_service.dto.response.DoctorResponse;
import com.smart_caresync_system.appointment_service.dto.response.PatientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service")
public interface PatientProxy {

    @GetMapping("/api/v1/patients/{patientId}")
    PatientResponse retrievePatientDetails(@PathVariable("patientId") Long patientId);
}
