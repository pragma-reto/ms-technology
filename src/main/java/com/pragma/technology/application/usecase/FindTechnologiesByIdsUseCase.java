package com.pragma.technology.application.usecase;

import com.pragma.technology.application.dto.GetAllByIdsCommand;
import com.pragma.technology.application.dto.TechnologyResponse;
import com.pragma.technology.domain.port.out.TechnologyRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class FindTechnologiesByIdsUseCase {
    private final TechnologyRepositoryPort port;

    public Flux<TechnologyResponse> execute(GetAllByIdsCommand request) {
        log.debug("Buscando {} tecnologías por IDs", request.ids().size());

        return port.findByIds(request.ids())
                .map(technology -> new TechnologyResponse(
                        technology.getId(),
                        technology.getName(),
                        technology.getDescription()

                ))
                .doOnComplete(() ->
                        log.debug("Búsqueda batch completada")
                );
    }
}
