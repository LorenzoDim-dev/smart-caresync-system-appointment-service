package com.smart_caresync_system.appointment_service.config.kafka.producer;

import com.smart_caresync_system.appointment_service.config.kafka.event.AppointmentApprovedEvent;
import com.smart_caresync_system.appointment_service.config.kafka.event.AppointmentRejectedEvent;
import com.smart_caresync_system.appointment_service.config.kafka.event.AppointmentScheduledEvent;
import com.smart_caresync_system.appointment_service.constant.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishAppointmentScheduledEvent(AppointmentScheduledEvent appointmentScheduledEvent) {

        kafkaTemplate.send(KafkaTopics.APPOINTMENT_SCHEDULED, appointmentScheduledEvent.getAppointmentId().toString(), appointmentScheduledEvent);

        log.info("Appointment event sent : {}", appointmentScheduledEvent);
    }

    public void publishAppointmentApprovedEvent(AppointmentApprovedEvent appointmentApprovedEvent) {

        kafkaTemplate.send(KafkaTopics.APPOINTMENT_APPROVED, appointmentApprovedEvent.getAppointmentId().toString(), appointmentApprovedEvent);

        log.info("Appointment approved successfully : {}", appointmentApprovedEvent);

    }

    public void publishAppointmentRejectedEvent(AppointmentRejectedEvent appointmentRejectedEvent) {

        kafkaTemplate.send(KafkaTopics.APPOINTMENT_REJECTED, appointmentRejectedEvent.getAppointmentId().toString(), appointmentRejectedEvent);

        log.info("The doctor is unavailable, the appointment was rejected : {}", appointmentRejectedEvent);

    }

}
