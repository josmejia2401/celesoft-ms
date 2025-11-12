package com.celesoft.utils.security;

import com.celesoft.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationWebFilter implements WebFilter {
    private static final Map<String, Set<String>> PUBLIC_ENDPOINTS = Map.ofEntries(
            Map.entry("/auth/login", Set.of("POST")),
            Map.entry("/auth/logout", Set.of("POST")),
            Map.entry("/users", Set.of("POST")),
            Map.entry("/actuator/health", Set.of("GET")),
            Map.entry("/v3/api-docs", Set.of("GET")),
            Map.entry("/swagger-ui", Set.of("GET")),
            Map.entry("/swagger-ui.html", Set.of("GET"))
    );

    private final JwtUtil jwtUtil;

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
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        final String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.decodeToken(token);
            if (claims.getExpiration() != null && claims.getExpiration().before(new Date())) {
                log.warn("Token expirado para usuario {}", claims.getSubject());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            log.debug("Token válido. Usuario: {}, Role: {}", claims.getSubject(), claims.get("role"));
            exchange.getAttributes().put("jwtClaims", claims);
            return chain.filter(exchange);

        } catch (Exception e) {
            log.warn("Token inválido o error al decodificar: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublicEndpoint(String path, String method) {
        return PUBLIC_ENDPOINTS.entrySet().stream()
                .anyMatch(entry ->
                        path.startsWith(entry.getKey()) &&
                                entry.getValue().contains(method)
                );
    }
}