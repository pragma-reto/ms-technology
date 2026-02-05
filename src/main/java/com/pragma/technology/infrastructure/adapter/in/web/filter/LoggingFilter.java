package com.pragma.technology.infrastructure.adapter.in.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;


@Slf4j
@Component
public class LoggingFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        Instant start = Instant.now();
        String method = request.method().name();
        String path = request.path();

        log.info("→ Request: {} {}", method, path);

        return next.handle(request)
                .doOnSuccess(response -> {
                    Duration duration = Duration.between(start, Instant.now());
                    log.info("← Response: {} {} - Status: {} - Duration: {}ms",
                            method,
                            path,
                            response.statusCode().value(),
                            duration.toMillis()
                    );
                })
                .doOnError(error -> {
                    Duration duration = Duration.between(start, Instant.now());
                    log.error("✖ Error: {} {} - Duration: {}ms - Error: {}",
                            method,
                            path,
                            duration.toMillis(),
                            error.getMessage()
                    );
                });
    }
}
