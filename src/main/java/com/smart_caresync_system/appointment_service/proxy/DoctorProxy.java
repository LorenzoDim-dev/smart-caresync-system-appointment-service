package com.smart_caresync_system.appointment_service.proxy;

import com.smart_caresync_system.appointment_service.dto.response.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service")
public interface DoctorProxy {

    @GetMapping("/api/v1/doctors/{doctorId}")
    DoctorResponse retrieveDoctorDetails(@PathVariable("doctorId") Long doctorId);
}
