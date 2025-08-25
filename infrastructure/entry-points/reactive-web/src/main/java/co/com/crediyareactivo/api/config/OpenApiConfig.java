package co.com.crediyareactivo.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Auteticacion",
                version = "1.0",
                description = "Documentacion de endpoints para gestionar autenticacion de usuarios",
                contact = @Contact(name = "Michael Acevedo", email = "michaelacevedoruiz48@gmail.com")
        ),
        servers = @Server(url = "http://localhost:8080", description = "Servidor Local")
)
public class OpenApiConfig {
}

