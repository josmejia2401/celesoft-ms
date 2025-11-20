package com.celesoft.utils.security;

import com.celesoft.auth.dto.TokenDTO;
import com.celesoft.auth.repository.TokenAuthRepository;
import com.celesoft.utils.JwtUtil;
import com.celesoft.utils.exceptions.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
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

    private final ObjectMapper objectMapper;
    private final TokenAuthRepository tokenAuthRepository;
    private final Cache<Long, TokenDTO> tokenCache;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        final String path = exchange.getRequest().getURI().getPath();
        final String method = Objects.requireNonNullElse(exchange.getRequest().getMethod(), HttpMethod.GET).name();

        if (isPublicEndpoint(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        return Mono.defer(() -> decodeAndValidateToken(token))
                .flatMap(claims -> validateTokenInDatabase(token, claims))
                .flatMap(claims -> {
                    exchange.getAttributes().put("jwtClaims", claims);
                    return chain.filter(exchange);
                })
                .onErrorResume(ex -> {
                    log.warn("Auth error: {}", ex.getMessage());
                    return unauthorized(exchange, ex.getMessage());
                });
    }

    private Mono<Claims> decodeAndValidateToken(String token) {
        return Mono.fromCallable(() -> JwtUtil.decodeToken(token))
                .flatMap(claims -> {
                    if (claims.getExpiration().before(new Date())) {
                        return Mono.error(new RuntimeException("Token expired"));
                    }
                    return Mono.just(claims);
                });
    }

    private Mono<Claims> validateTokenInDatabase(String token, Claims claims) {
        Long jti = Long.valueOf(claims.getId());

        TokenDTO cached = tokenCache.getIfPresent(jti);
        if (cached != null && cached.getAccessToken().equals(token)) {
            return Mono.just(claims);
        }

        return tokenAuthRepository.findById(jti)
                .switchIfEmpty(Mono.error(new RuntimeException("Token not found")))
                .flatMap(entity -> {
                    if (!entity.getAccessToken().equals(token)) {
                        return Mono.error(new RuntimeException("Token does not match"));
                    }

                    tokenCache.put(jti, TokenDTO
                            .builder()
                                    .accessToken(entity.getAccessToken())
                                    .appName(entity.getAppName())
                                    .audience(entity.getAudience())
                                    .id(entity.getId())
                                    .createdAt(entity.getCreatedAt())
                                    .expiresAt(entity.getExpiresAt())
                                    .userId(entity.getUserId())
                            .build());
                    return Mono.just(claims);
                });
    }

    private boolean isPublicEndpoint(String path, String method) {
        return PUBLIC_ENDPOINTS.entrySet().stream()
                .anyMatch(entry -> path.endsWith(entry.getKey()) && entry.getValue().contains(method));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(401)
                .error("Unauthorized")
                .message(message)
                .build();

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(error);
        } catch (Exception e) {
            bytes = ("{\"message\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        }
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }
}
