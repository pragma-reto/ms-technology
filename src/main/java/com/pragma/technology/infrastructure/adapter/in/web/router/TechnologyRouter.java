package com.pragma.technology.infrastructure.adapter.in.web.router;

import com.pragma.technology.infrastructure.adapter.in.web.handler.TechnologyHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class TechnologyRouter {

    private final static String API_VERSION = "/api/v1/";
    private final static String COMPONENT = "technology";

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/technology",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "crearTecnologia",
                            summary = "Crear una nueva tecnología",
                            description = "Crea una nueva tecnología en el catálogo",
                            tags = {"Tecnologías"},
                            requestBody = @RequestBody(
                                    description = "Datos de la tecnología a crear",
                                    required = true
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Tecnología creada exitosamente"),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                                    @ApiResponse(responseCode = "409", description = "Tecnología duplicada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/technology/{id}",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "buscarTecnologiaPorId",
                            summary = "Buscar tecnología por ID",
                            description = "Obtiene una tecnología específica por su UUID",
                            tags = {"Tecnologías"},
                            parameters = {
                                    @Parameter(name = "id", description = "UUID de la tecnología", required = true, in = ParameterIn.PATH)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Tecnología encontrada"),
                                    @ApiResponse(responseCode = "404", description = "Tecnología no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/technology",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "listarTecnologias",
                            summary = "Listar todas las tecnologías",
                            description = "Obtiene el listado completo de tecnologías disponibles",
                            tags = {"Tecnologías"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Listado de tecnologías")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/technology/batch",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "buscarTecnologiasPorIds",
                            summary = "Búsqueda batch de tecnologías",
                            description = "Busca múltiples tecnologías por sus UUIDs",
                            tags = {"Tecnologías"},
                            requestBody = @RequestBody(
                                    description = "Lista de UUIDs de tecnologías",
                                    required = true
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Tecnologías encontradas")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> technologyRouters(TechnologyHandler handler) {
        return route()
                .path(API_VERSION + COMPONENT, builder -> builder
                        .POST("",
                                accept(MediaType.APPLICATION_JSON)
                                        .and(contentType(MediaType.APPLICATION_JSON)),
                                handler::create)
                        .POST( "/batch",
                                accept(MediaType.APPLICATION_JSON)
                                        .and(contentType(MediaType.APPLICATION_JSON)),
                                handler::findByIds)
                        .GET("/{id}",
                                accept(MediaType.APPLICATION_JSON),
                                handler::findById)
                        .GET("",
                                accept(MediaType.APPLICATION_JSON),
                                handler::getAll)
                )
                .build();
    }
}
