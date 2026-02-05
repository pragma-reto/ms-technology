package com.pragma.technology.application.usecase;

import com.pragma.technology.application.dto.TechnologyResponse;
import com.pragma.technology.domain.port.out.TechnologyRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
public class ListTechnologiesUseCase {

    private final TechnologyRepositoryPort port;

    public Flux<TechnologyResponse> execute() {
        log.debug("Listando todas las tecnologías");

        return port.findAll()
                .map(technology -> new TechnologyResponse(
                        technology.getId(),
                        technology.getName(),
                        technology.getDescription()
                        )
                )
                .doOnComplete(() -> log.debug("Listado de tecnologías completado"));
    }
}
