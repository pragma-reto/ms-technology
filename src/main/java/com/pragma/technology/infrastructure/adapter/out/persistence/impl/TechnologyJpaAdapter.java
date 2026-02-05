package com.pragma.technology.infrastructure.adapter.out.persistence.impl;

import com.pragma.technology.domain.model.Technology;
import com.pragma.technology.domain.port.out.TechnologyRepositoryPort;
import com.pragma.technology.infrastructure.adapter.out.persistence.mapper.TechnologyPersistenceMapper;
import com.pragma.technology.infrastructure.adapter.out.persistence.repository.TechnologyR2dbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class TechnologyJpaAdapter implements TechnologyRepositoryPort {
    private final TechnologyR2dbcRepository repository;

    @Override
    public Mono<Technology> save(Technology technology) {
        log.debug("Guardando tecnologia en BD: {}", technology.getName());

        return Mono.just(technology)
                .map(TechnologyPersistenceMapper.toEntity())
                .flatMap(repository::save)
                .map(TechnologyPersistenceMapper.toDomain())
                .doOnSuccess( saved ->
                        {
                            log.debug("tecnologia guardada id {}", saved.getId());
                        }
                );
    }

    @Override
    public Mono<Technology> findById(UUID id) {
        log.debug("Buscando tecnologia: {}", id);

        return repository.findById(id)
                .map(TechnologyPersistenceMapper.toDomain())
                .doOnNext(found ->
                                log.debug("Tecnologia encontrada: {}", found.getName())
                );
    }

    @Override
    public Mono<Boolean> existTechnologyByName(String name) {
        log.debug("Verificando existencia de tecnología con nombre={}", name);

        return repository.existsByNameIgnoreCaseAsInt(name)
                .map(value -> value == 1L);
    }

    @Override
    public Flux<Technology> findAll() {
        log.debug("Buscando todas las tecnologías");

        return repository.findAll()
                .map(TechnologyPersistenceMapper.toDomain());
    }

    @Override
    public Flux<Technology> findByIds(List<String> ids) {
        log.debug("Buscando tecnologías por {} IDs", ids.size());

        return repository.findAllByIdIn(ids)
                .map(TechnologyPersistenceMapper.toDomain());
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        log.debug("Eliminando tecnología con id={}", id);

        return repository.deleteById(id)
                .doOnSuccess(v ->
                        log.debug("Tecnología eliminada con id={}", id)
                );
    }
}
