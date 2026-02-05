package com.pragma.technology.infrastructure.config;

import com.pragma.technology.application.usecase.CreateTechnologyUseCase;
import com.pragma.technology.application.usecase.FindTechnologiesByIdsUseCase;
import com.pragma.technology.application.usecase.FindTechnologyByIdUseCase;
import com.pragma.technology.application.usecase.ListTechnologiesUseCase;
import com.pragma.technology.domain.port.out.TechnologyRepositoryPort;
import com.pragma.technology.infrastructure.adapter.out.persistence.impl.TechnologyJpaAdapter;
import com.pragma.technology.infrastructure.adapter.out.persistence.repository.TechnologyR2dbcRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public TechnologyRepositoryPort technologyRepositoryPort(TechnologyR2dbcRepository repository) {
        return new TechnologyJpaAdapter(repository);
    }


    @Bean
    public CreateTechnologyUseCase createTechnologyUseCase(TechnologyRepositoryPort repositoryPort) {
        return new CreateTechnologyUseCase(repositoryPort);
    }

    @Bean
    public FindTechnologyByIdUseCase findTechnologyByIdUseCase(TechnologyRepositoryPort repositoryPort) {
        return new FindTechnologyByIdUseCase(repositoryPort);
    }

    @Bean
    public ListTechnologiesUseCase listTechnologiesUseCase(TechnologyRepositoryPort repositoryPort) {
        return new ListTechnologiesUseCase(repositoryPort);
    }

    @Bean
    public FindTechnologiesByIdsUseCase findTechnologiesByIdsUseCase(TechnologyRepositoryPort repositoryPort) {
        return new FindTechnologiesByIdsUseCase(repositoryPort);
    }
}
