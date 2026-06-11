package com.smart_caresync_system.appointment_service.repository;

import com.smart_caresync_system.appointment_service.constant.AppointmentStatus;
import com.smart_caresync_system.appointment_service.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorId(Long doctorId);


    List<Appointment> findByPatientId(Long patientId);


    boolean existsByDoctorIdAndAppointmentDateTimeAndStatusIn(Long doctorId, LocalDateTime appointmentDateTime,List<AppointmentStatus> statuses);
}
