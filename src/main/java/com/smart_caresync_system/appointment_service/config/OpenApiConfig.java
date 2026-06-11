package com.smart_caresync_system.appointment_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI patientServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Appointment Service API")
                        .description("Microservice for appointment management")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Smart CareSync System")
                                .email("support@smartcaresync.com")
                        )
                );
    }
}
