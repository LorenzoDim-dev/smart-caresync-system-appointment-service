package com.smart_caresync_system.appointment_service.config.kafka.producer;

import com.smart_caresync_system.appointment_service.config.kafka.event.AppointmentApprovedEvent;
import com.smart_caresync_system.appointment_service.config.kafka.event.AppointmentRejectedEvent;
import com.smart_caresync_system.appointment_service.config.kafka.event.AppointmentScheduledEvent;
import com.smart_caresync_system.appointment_service.constant.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishAppointmentScheduledEvent(AppointmentScheduledEvent appointmentScheduledEvent) {

        Message<AppointmentScheduledEvent> message = MessageBuilder.withPayload(appointmentScheduledEvent)
                .setHeader(KafkaHeaders.TOPIC, KafkaTopics.APPOINTMENT_SCHEDULED)
                .setHeader(KafkaHeaders.KEY, appointmentScheduledEvent.getAppointmentId().toString())
                .setHeader("event-type", "AppointmentScheduledEvent")
                .setHeader("event-version", "v1")
                .setHeader("producer-service", "appointment-service")
                .setHeader("correlation-id", UUID.randomUUID().toString())
                .build();

        kafkaTemplate.send(message);

//        kafkaTemplate.send(KafkaTopics.APPOINTMENT_SCHEDULED, appointmentScheduledEvent.getAppointmentId().toString(), appointmentScheduledEvent);

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
