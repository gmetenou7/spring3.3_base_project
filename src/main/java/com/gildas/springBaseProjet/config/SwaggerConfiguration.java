package com.gildas.springBaseProjet.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "http://localhost:8080/api/1.0/",
                        description = "Default Server URL")},
        info = @Info(
                title = "SPRINGBOOT 3.3 INITIAL PROJET",
                version = "1.0",
                description = "springboot 3.3 projet initial sous docker, jwt(token and refresh token)",
                license = @License(name = "Apache 2.0", url = ""),
                contact = @Contact(
                        url = "https://www.linkedin.com/in/gildas-nguekeng-metenou-056225176/",
                        name = "Gildas Metenou",
                        email = "gmetenou7@gmail.com"
                )
        )
)

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfiguration {
}
