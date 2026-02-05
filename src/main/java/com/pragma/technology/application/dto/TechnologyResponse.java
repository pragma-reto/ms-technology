package com.pragma.technology.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Respuesta con los datos de una tecnología")
public record TechnologyResponse(
        @Schema(
                description = "Identificador único de la tecnología",
                example = "550e8400-e29b-41d4-a716-446655440000",
                format = "uuid"
        )
        String id,
        @Schema(
                description = "Nombre de la tecnología",
                example = "Java",
                maxLength = 50
        )
        String name,
        @Schema(
                description = "Descripción de la tecnología",
                example = "Lenguaje de programación orientado a objetos",
                maxLength = 90
        )
        String description
) {
}
