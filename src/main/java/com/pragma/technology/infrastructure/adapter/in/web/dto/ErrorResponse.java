package com.pragma.technology.infrastructure.adapter.in.web.dto;

public record ErrorResponse (
        String type,
        String title,
        int status
) {
}
