package com.pragma.technology.domain.port.out;

import com.pragma.technology.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface TechnologyRepositoryPort {

    Mono<Technology> save(Technology technology);

    Mono<Technology> findById(UUID id);

    Mono<Boolean> existTechnologyByName(String name);

    Flux<Technology> findAll();

    Flux<Technology> findByIds(List<String> ids);

    Mono<Void> deleteById(UUID id);

}
