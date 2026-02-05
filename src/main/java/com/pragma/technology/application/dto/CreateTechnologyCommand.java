package com.pragma.technology.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Comando para crear una nueva tecnología")
public record CreateTechnologyCommand(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre debe tener un máximo de 50 caracteres")
        @Schema(
                description = "Nombre de la tecnología",
                example = "Java",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 50
        )
        String name,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 90, message = "La descripción debe tener un máximo de 90 caracteres")
        @Schema(
                description = "Descripción detallada de la tecnología",
                example = "Lenguaje de programación orientado a objetos",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 90
        )
        String description
) {
}
