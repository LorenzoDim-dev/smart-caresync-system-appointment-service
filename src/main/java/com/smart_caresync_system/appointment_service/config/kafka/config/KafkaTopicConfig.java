package com.smart_caresync_system.appointment_service.config.kafka.config;

import com.smart_caresync_system.appointment_service.constant.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic appointmentScheduledTopic(){
        return TopicBuilder.name(KafkaTopics.APPOINTMENT_SCHEDULED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic appointmentApprovedTopic(){
        return TopicBuilder.name(KafkaTopics.APPOINTMENT_APPROVED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic appointmentRejectedTopic(){
        return TopicBuilder.name(KafkaTopics.APPOINTMENT_REJECTED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
