package com.pragma.technology.application.usecase;

import com.pragma.technology.application.dto.TechnologyResponse;
import com.pragma.technology.domain.exception.TechnologyNotFoundException;
import com.pragma.technology.domain.port.out.TechnologyRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor

public class FindTechnologyByIdUseCase {
    private final TechnologyRepositoryPort port;


    public Mono<TechnologyResponse> execute(UUID id) {
        log.debug("Buscando tecnologia con id={}", id);

        return port.findById(id)
                .switchIfEmpty(Mono.error(new TechnologyNotFoundException(id)))
                .map(technology -> new TechnologyResponse(
                        technology.getId(),
                        technology.getName(),
                        technology.getDescription()
                ))
                .doOnSuccess(response ->
                        log.debug("Tecnologia encontrada: {}", response.name())
                );
    }
}
