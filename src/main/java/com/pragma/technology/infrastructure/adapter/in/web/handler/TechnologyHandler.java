package com.pragma.technology.infrastructure.adapter.in.web.handler;

import com.pragma.technology.application.dto.CreateTechnologyCommand;
import com.pragma.technology.application.dto.GetAllByIdsCommand;
import com.pragma.technology.application.dto.TechnologyResponse;
import com.pragma.technology.application.usecase.CreateTechnologyUseCase;
import com.pragma.technology.application.usecase.FindTechnologiesByIdsUseCase;
import com.pragma.technology.application.usecase.FindTechnologyByIdUseCase;
import com.pragma.technology.application.usecase.ListTechnologiesUseCase;
import com.pragma.technology.domain.exception.TechnologyDuplicateException;
import com.pragma.technology.domain.exception.TechnologyInvalidException;
import com.pragma.technology.domain.exception.TechnologyNotFoundException;
import com.pragma.technology.infrastructure.adapter.in.web.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TechnologyHandler {
    private final CreateTechnologyUseCase createTechnologyUseCase;
    private final FindTechnologyByIdUseCase findTechnologyByIdUseCase;
    private final ListTechnologiesUseCase listTechnologiesUseCase;
    private final FindTechnologiesByIdsUseCase findTechnologiesByIdsUseCase;

    @Value("${api.version}")
    private static String API_VERSION;
    @Value("${spring.application.name}")
    private static String COMPONENT;

    public Mono<ServerResponse> create (ServerRequest request) {
        log.info("Recibida petición para crear tecnología");

        return request.bodyToMono(CreateTechnologyCommand.class)
                .flatMap(createTechnologyUseCase::execute)
                .flatMap(response -> created(
                        URI.create(API_VERSION + COMPONENT + "/" + response.id())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                )
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String idParam = request.pathVariable("id");
        log.info("Recibida petición para buscar tecnología con id={}", idParam);

        return Mono.fromCallable(() -> UUID.fromString(idParam))
                .flatMap(findTechnologyByIdUseCase::execute)
                .flatMap(response -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                )
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        log.info("Recibida petición para listar tecnologías");

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(listTechnologiesUseCase.execute(), TechnologyResponse.class)
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> findByIds(ServerRequest request) {
        log.info("Recibida petición para búsqueda batch de tecnologías");

        return request.bodyToMono(GetAllByIdsCommand.class)
                .flatMapMany(findTechnologiesByIdsUseCase::execute)
                .collectList()
                .flatMap(technology -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(technology)
                )
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        log.error("Error procesando request: {}", error.getMessage(), error);

        return switch (error) {
            case TechnologyInvalidException e ->
                    badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorResponse(
                                    "TECNOLOGIA_INVALIDA",
                                    e.getMessage(),
                                    400
                            ));

            case TechnologyDuplicateException e ->
                    status(409)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorResponse(
                                    "TECNOLOGIA_DUPLICADA",
                                    e.getMessage(),
                                    409
                            ));

            case TechnologyNotFoundException e ->
                    status(404)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorResponse(
                                    "TECNOLOGIA_NO_ENCONTRADA",
                                    e.getMessage(),
                                    404
                            ));

            case IllegalArgumentException e when e.getMessage().contains("Invalid UUID") ->
                    badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorResponse(
                                    "ID_INVALIDO",
                                    "El ID proporcionado no es un UUID válido",
                                    400
                            ));

            default ->
                    status(500)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorResponse(
                                    "ERROR_INTERNO",
                                    "Error interno del servidor",
                                    500
                            ));
        };
    }
}
