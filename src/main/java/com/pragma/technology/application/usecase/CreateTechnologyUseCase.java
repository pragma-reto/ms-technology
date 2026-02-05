package com.pragma.technology.application.usecase;

import com.pragma.technology.application.dto.CreateTechnologyCommand;
import com.pragma.technology.application.dto.TechnologyResponse;
import com.pragma.technology.domain.exception.TechnologyDuplicateException;
import com.pragma.technology.domain.model.Technology;
import com.pragma.technology.domain.port.out.TechnologyRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class CreateTechnologyUseCase {
    private final TechnologyRepositoryPort port;

    public Mono<TechnologyResponse> execute(CreateTechnologyCommand command) {
        log.debug("Ejecutando caso de uso: crear tecnología con nombre={}", command.name());

        return validateNameUnique(command.name())
                .then(createTechnology(command))
                .flatMap(port::save)
                .map(mapperResponse())
                .doOnSuccess(response ->
                        log.info("Tecnología creada exitosamente con id={}", response.id())
                )
                .doOnError(error ->
                        log.error("Error al crear tecnología: {}", error.getMessage())
                );
    }

    private Mono<Void> validateNameUnique(String name) {
        return port.existTechnologyByName(name)
                .flatMap(exist -> exist
                        ? Mono.error(new TechnologyDuplicateException(name))
                        : Mono.empty()
                );
    }

    private Mono<Technology> createTechnology(CreateTechnologyCommand command) {
        return Mono.fromCallable(() ->
                Technology.create(command.name(), command.description())
        );
    }

    private Function<Technology, TechnologyResponse> mapperResponse() {
        return technology -> new TechnologyResponse(
                technology.getId(),
                technology.getName(),
                technology.getDescription()
        );
    }
}
