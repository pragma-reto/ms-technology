package com.pragma.technology.infrastructure.adapter.out.persistence.repository;

import com.pragma.technology.infrastructure.adapter.out.persistence.entity.TechnologyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface TechnologyR2dbcRepository extends ReactiveCrudRepository<TechnologyEntity, UUID> {
    @Query("SELECT EXISTS(SELECT 1 FROM technology WHERE LOWER(name) = LOWER(:name))")
    Mono<Integer> existsByNameIgnoreCaseAsInt(String name);

    @Query("SELECT * FROM technology WHERE id IN (:ids)")
    Flux<TechnologyEntity> findAllByIdIn(List<String> ids);

}
