package com.pragma.technology.infrastructure.adapter.out.persistence.mapper;

import com.pragma.technology.domain.model.Technology;
import com.pragma.technology.infrastructure.adapter.out.persistence.entity.TechnologyEntity;

import java.util.UUID;
import java.util.function.Function;

public class TechnologyPersistenceMapper {

    public static Function<Technology, TechnologyEntity> toEntity() {
        return technology -> TechnologyEntity.builder()
                .id(technology.getId())
                .name(technology.getName())
                .description(technology.getDescription())
                .build();
    }

    public static Function<TechnologyEntity, Technology> toDomain() {
        return entity -> Technology.reconstructor(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
