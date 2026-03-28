package com.esser.maintenanceapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Maintenance App API",
                version = "v1",
                description = "API de gestion de maintenance",
                contact = @Contact(name = "Maintenance App"),
                license = @License(name = "Apache 2.0")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local")
        }
)
public class OpenApiConfig {
}