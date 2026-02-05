package com.pragma.technology.domain.exception;

public class TechnologyDuplicateException extends RuntimeException {
    public TechnologyDuplicateException(String name) {
        super("Ya existe una tecnología con el nombre: " + name);
    }
}
