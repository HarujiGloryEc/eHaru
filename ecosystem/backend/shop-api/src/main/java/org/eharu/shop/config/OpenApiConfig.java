package org.eharu.shop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Haru Shop API",
        version = "1.0.0",
        description = "REST API for the Haru Shop e-commerce platform",
        contact = @Contact(name = "Haru Shop", email = "harujiburke@gmail.com")
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local development"),
        @Server(url = "/", description = "Current server")
    }
)
public class OpenApiConfig {
}
