package com.pragma.technology.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // Deshabilitar CSRF (común en APIs REST stateless)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Configuración de autorización
                .authorizeExchange(exchanges -> exchanges
                        // Endpoints públicos (no requieren autenticación)
                        .pathMatchers(
                                "/actuator/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/api-docs/**"
                        ).permitAll()

                        // TEMPORAL: Permitir todos los endpoints de tecnologías
                        // TODO: Cambiar a .authenticated() cuando se implemente JWT
                        .pathMatchers("/api/v1/technology/**").permitAll()

                        .anyExchange().authenticated()
                )

                // .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)

                .build();
    }

    /*
     * IMPLEMENTACIÓN DE JWT -
     *
     * Componentes necesarios:
     * 1. JwtAuthenticationManager - Valida el token JWT
     * 2. JwtServerAuthenticationConverter - Extrae el token del header
     * 3. JwtUtil - Utilidad para generar y validar tokens
     *
     * Ejemplo de uso:
     *
     * @Bean
     * public AuthenticationWebFilter jwtAuthenticationFilter() {
     *     AuthenticationWebFilter filter = new AuthenticationWebFilter(jwtAuthenticationManager);
     *     filter.setServerAuthenticationConverter(new JwtServerAuthenticationConverter());
     *     return filter;
     * }
     *
     * Para generar tokens JWT:
     * - Crear endpoint POST /api/v1/auth/login
     * - Validar credenciales
     * - Generar token con JwtUtil
     * - Retornar token al cliente
     *
     * El cliente debe enviar el token en cada petición:
     * Authorization: Bearer <token>
     */
}
