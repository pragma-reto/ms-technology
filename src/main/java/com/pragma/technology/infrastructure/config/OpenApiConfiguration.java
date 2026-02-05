package com.pragma.technology.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Value("${server.port:8081}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Technology API")
                        .version("1.0.0")
                        .description("""
                        Microservicio de Gestión de Tecnologías
                        
                        Este microservicio es parte del sistema On-Class y gestiona el catálogo 
                        maestro de tecnologías disponibles para asociar a las capacidades de los bootcamps.
                        
                        **Características:**
                        - Arquitectura Hexagonal
                        - Programación Funcional y Reactiva (WebFlux)
                        - Base de datos reactiva (R2DBC MySQL)
                        - Autenticación JWT
                        """)
                        .contact(new Contact()
                                .name("Jonatan Restrepo")
                                .email("jonatan.restrepo@pragma.com.co")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de desarrollo local")
                ));
    }

    @Bean
    public GroupedOpenApi technologyApi() {
        return GroupedOpenApi.builder()
                .group("technology")
                .pathsToMatch("/api/v1/technology/**")
                .build();
    }
}
