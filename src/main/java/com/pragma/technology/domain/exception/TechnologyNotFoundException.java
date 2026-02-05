package com.pragma.technology.domain.exception;

import java.util.UUID;

public class TechnologyNotFoundException extends RuntimeException {
    public TechnologyNotFoundException(UUID id) {
        super("No se encontró la tecnología con id: " + id);
    }
}
