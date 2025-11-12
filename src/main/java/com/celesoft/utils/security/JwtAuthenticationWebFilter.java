package com.celesoft.utils.security;

import com.celesoft.utils.JwtUtil;
import com.celesoft.utils.exceptions.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationWebFilter implements WebFilter {
    private static final Map<String, Set<String>> PUBLIC_ENDPOINTS = Map.ofEntries(
            Map.entry("/auth/login", Set.of("POST")),
            Map.entry("/api/v1/users", Set.of("POST")),
            Map.entry("/actuator/health", Set.of("GET")),
            Map.entry("/v3/api-docs", Set.of("GET")),
            Map.entry("/swagger-ui", Set.of("GET")),
            Map.entry("/swagger-ui.html", Set.of("GET"))
    );

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper; // inyecta jackson

    @Override
    @NullMarked
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String path = exchange.getRequest().getURI().getPath();
        final String method = Objects.requireNonNullElse(exchange.getRequest().getMethod(), HttpMethod.GET).name();

        if (isPublicEndpoint(path, method)) {
            log.trace("Endpoint público: {} {}", method, path);
            return chain.filter(exchange);
        }

        final String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Falta header Authorization o formato inválido en {}", path);
            return writeError(exchange, HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        final String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.decodeToken(token);
            if (claims.getExpiration() != null && claims.getExpiration().before(new java.util.Date())) {
                log.warn("Token expirado para usuario {}", claims.getSubject());
                return writeError(exchange, HttpStatus.UNAUTHORIZED, "Token expired");
            }

            log.debug("Token válido. Usuario: {}, Role: {}", claims.getSubject(), claims.get("role"));
            exchange.getAttributes().put("jwtClaims", claims);
            return chain.filter(exchange);

        } catch (Exception e) {
            log.warn("Token inválido o error al decodificar: {}", e.getMessage());
            // No exponer e.getMessage() si contiene info sensible; aquí usamos mensaje genérico
            return writeError(exchange, HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    private boolean isPublicEndpoint(String path, String method) {
        return PUBLIC_ENDPOINTS.entrySet().stream()
                .anyMatch(entry ->
                        path.startsWith(entry.getKey()) &&
                                entry.getValue().contains(method)
                );
    }

    /**
     * Escribe un body JSON de error y devuelve el Mono<Void> que representa la escritura.
     */
    private Mono<Void> writeError(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiError body =  ApiError
                .builder()
                .error(status.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message(message)
                .status(status.value())
                .build();

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(body);
        } catch (Exception e) {
            String fallback = "{\"status\":" + status.value() + ",\"message\":\"" + message + "\"}";
            bytes = fallback.getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer))
                .doOnError(err -> log.error("Error writing response body", err));
    }
}
