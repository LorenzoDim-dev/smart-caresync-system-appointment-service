package com.smart_caresync_system.appointment_service.service;

import com.smart_caresync_system.appointment_service.config.kafka.event.AppointmentApprovedEvent;
import com.smart_caresync_system.appointment_service.config.kafka.event.AppointmentRejectedEvent;
import com.smart_caresync_system.appointment_service.config.kafka.event.AppointmentScheduledEvent;
import com.smart_caresync_system.appointment_service.config.kafka.producer.AppointmentProducer;
import com.smart_caresync_system.appointment_service.constant.AppointmentStatus;
import com.smart_caresync_system.appointment_service.dto.request.AppointmentRequest;
import com.smart_caresync_system.appointment_service.dto.response.AppointmentResponse;
import com.smart_caresync_system.appointment_service.dto.response.DoctorResponse;
import com.smart_caresync_system.appointment_service.dto.response.PatientResponse;
import com.smart_caresync_system.appointment_service.exception.*;
import com.smart_caresync_system.appointment_service.mapper.AppointmentManualMapper;
import com.smart_caresync_system.appointment_service.mapper.AppointmentMapper;
import com.smart_caresync_system.appointment_service.model.Appointment;
import com.smart_caresync_system.appointment_service.proxy.DoctorProxy;
import com.smart_caresync_system.appointment_service.proxy.PatientProxy;
import com.smart_caresync_system.appointment_service.repository.AppointmentRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentManualMapper appointmentManualMapper;
    private final DoctorProxy doctorProxy;
    private final PatientProxy patientProxy;
    private final AppointmentProducer  appointmentProducer;

    @Override
    public AppointmentResponse scheduleAppointment(AppointmentRequest appointmentRequest) {
        //check if the slot for the appointment is already occupied
        boolean slotOccupied = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(appointmentRequest.getDoctorId(),
                appointmentRequest.getAppointmentDateTime(), List.of(AppointmentStatus.PENDING, AppointmentStatus.APPROVED));

        if (slotOccupied) {
            throw new DoctorNotAvailableException("Doctor with id " + appointmentRequest.getDoctorId() +
                    " is not available at " + appointmentRequest.getAppointmentDateTime());
        }

        Appointment appointment = appointmentMapper.toEntity(appointmentRequest);

        appointment.setStatus(AppointmentStatus.PENDING);

        //retrieve patient details
        PatientResponse patient = patientProxy.retrievePatientDetails(appointmentRequest.getPatientId());

        //retrieve doctor details
        DoctorResponse doctor = doctorProxy.retrieveDoctorDetails(appointmentRequest.getDoctorId());

        Appointment savedAppointment = appointmentRepository.save(appointment);

        //kafka event
        AppointmentScheduledEvent event = AppointmentScheduledEvent.builder()
                .appointmentId(savedAppointment.getAppointmentId())
                .patientId(savedAppointment.getPatientId())
                .doctorId(savedAppointment.getDoctorId())
                .appointmentDate(savedAppointment.getAppointmentDateTime())
                .build();

        appointmentProducer.publishAppointmentScheduledEvent(event);

        return AppointmentResponse.builder()
                .appointmentId(savedAppointment.getAppointmentId())
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(savedAppointment.getAppointmentDateTime())
                .status(savedAppointment.getStatus())
                .build();
    }

    @Override
    public List<AppointmentResponse> retrieveAppointmentsOfDoctor(Long doctorId) {
        //check if doctor exists
        doctorProxy.retrieveDoctorDetails(doctorId);

        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream()
                .map(appointmentManualMapper::toResponse)
                .toList();

    }

    @Override
    public List<AppointmentResponse> retrieveAppointmentsOfPatient(Long patientId) {
        //check if patient exists
        patientProxy.retrievePatientDetails(patientId);


        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

        return appointments.stream()
                .map(appointmentManualMapper::toResponse)
                .toList();


    }

    @Transactional
    @Override
    public void approveAppointment(Long appointmentId) {
        //retrieve appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with id " + appointmentId + " not found"));

        //check statuses
        if (appointment.getStatus() == AppointmentStatus.APPROVED) {
            throw new AppointmentAlreadyApprovedException(
                    "Appointment already approved"
            );
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new AppointmentCancelledException(
                    "Cannot approve a cancelled appointment"
            );
        }
        if (appointment.getStatus() == AppointmentStatus.VISITED) {
            throw new InvalidAppointmentStateException(
                    "Visited appointments cannot be approved again"
            );
        }
        //check if exists another appointment in that dateTime
        boolean approvedAppointmentExists =
                appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(
                        appointment.getDoctorId(),
                        appointment.getAppointmentDateTime(),
                        List.of(AppointmentStatus.APPROVED)
                );

        if (approvedAppointmentExists) {
            throw new DoctorNotAvailableException(
                    "Doctor already has an approved appointment at "
                            + appointment.getAppointmentDateTime()
            );
        }

        appointment.setStatus(AppointmentStatus.APPROVED);

        //kafka event
        AppointmentApprovedEvent event = AppointmentApprovedEvent.builder()
                .appointmentId(appointment.getAppointmentId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDateTime())
                .build();

        appointmentProducer.publishAppointmentApprovedEvent(event);
    }

    @Transactional
    @Override
    public void rejectAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with id " + appointmentId + " not found"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new AppointmentCancelledException(
                    "Appointment already cancelled"
            );
        }
        if (appointment.getStatus() == AppointmentStatus.VISITED) {
            throw new InvalidAppointmentStateException(
                    "Visited appointments cannot be cancelled"
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        //kafka event
        AppointmentRejectedEvent event = AppointmentRejectedEvent.builder()
                .appointmentId(appointment.getAppointmentId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDateTime())
                .build();

        appointmentProducer.publishAppointmentRejectedEvent(event);
    }

    @Transactional
    @Override
    public void markAsVisitedAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment with id " + appointmentId + " not found"
                ));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new AppointmentCancelledException("Cannot mark cancelled appointment as visited");
        }

        if (appointment.getStatus() == AppointmentStatus.VISITED) {
            throw new AppointmentAlreadyVisitedException("Appointment already marked as visited");
        }

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new InvalidAppointmentStateException(
                    "Only approved appointments can be marked as visited"
            );
        }

        appointment.setStatus(AppointmentStatus.VISITED);
        appointment.setVisitedAt(LocalDateTime.now());
    }
}
