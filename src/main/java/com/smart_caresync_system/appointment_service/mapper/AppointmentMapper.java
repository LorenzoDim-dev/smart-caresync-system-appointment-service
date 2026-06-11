package com.smart_caresync_system.appointment_service.mapper;

import com.smart_caresync_system.appointment_service.dto.request.AppointmentRequest;
import com.smart_caresync_system.appointment_service.dto.response.AppointmentResponse;
import com.smart_caresync_system.appointment_service.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    Appointment toEntity (AppointmentRequest appointmentRequest);

    List<AppointmentResponse> toResponseList (List<Appointment> appointments);
}
