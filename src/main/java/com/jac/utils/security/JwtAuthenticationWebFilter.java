package com.jac.utils.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.jac.auth.dto.TokenDTO;
import com.jac.auth.repository.TokenAuthRepository;
import com.jac.utils.JwtUtil;
import com.jac.utils.exceptions.ApiError;
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
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

        log.debug("[FILTER] Request received: {} {}", method, path);

        if (isPublicEndpoint(path, method)) {
            log.debug("[FILTER] Public endpoint detected -> skipping auth: {} {}", method, path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            log.warn("[FILTER] Authorization header missing for path {}", path);
            return unauthorized(exchange, "Missing Authorization header");
        }
        //USER 251120802280
        //TOKEN 251120066029

        if (!authHeader.startsWith("Bearer ")) {
            log.warn("[FILTER] Invalid Authorization format: {}", authHeader);
            return unauthorized(exchange, "Invalid Authorization format");
        }

        String token = authHeader.substring(7);
        log.debug("[FILTER] Extracted token: {}", token);

        return Mono.defer(() -> {
                    log.debug("[FILTER] Decoding token...");
                    return decodeAndValidateToken(token);
                })
                .flatMap(claims -> {
                    log.debug("[FILTER] Token decoded for subject={} jti={}", claims.getSubject(), claims.getId());
                    return validateTokenInDatabase(token, claims);
                })
                .flatMap(claims -> {
                    log.debug("[FILTER] Token validated successfully for user {}", claims.getSubject());
                    exchange.getAttributes().put("jwtClaims", claims);
                    return chain.filter(exchange);
                })
                .onErrorResume(ex -> {
                    log.warn("[FILTER] Authentication error: {}", ex.getMessage());
                    return unauthorized(exchange, ex.getMessage());
                });
    }

    private Mono<Claims> decodeAndValidateToken(String token) {
        log.debug("[TOKEN] Decoding and validating expiration...");
        return Mono.fromCallable(() -> JwtUtil.decodeToken(token))
                .flatMap(claims -> {
                    log.debug("[TOKEN] Claims decoded: sub={} jti={} exp={}",
                            claims.getSubject(), claims.getId(), claims.getExpiration());

                    if (claims.getExpiration().before(new Date())) {
                        log.warn("[TOKEN] Token expired at {}", claims.getExpiration());
                        return Mono.error(new RuntimeException("Token expired"));
                    }
                    return Mono.just(claims);
                });
    }

    private Mono<Claims> validateTokenInDatabase(String token, Claims claims) {
        Long jti = Long.valueOf(claims.getId());

        log.debug("[DB] Looking for token in cache: jti={}", jti);

        TokenDTO cached = tokenCache.getIfPresent(jti);
        if (cached != null) {
            log.debug("[DB] Token found in cache for jti={}", jti);

            if (cached.getAccessToken().equals(token)) {
                log.debug("[DB] Cached token matches -> OK");
                return Mono.just(claims);
            } else {
                log.warn("[DB] Cached token mismatch for jti={}", jti);
            }
        }

        log.debug("[DB] Token not cached, searching in database... jti={}", jti);

        return tokenAuthRepository.findById(jti)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("[DB] Token NOT FOUND in database for jti={}", jti);
                    return Mono.error(new RuntimeException("Token not found"));
                }))
                .flatMap(entity -> {
                    log.debug("[DB] Token found in DB for jti={} | comparing tokens...", jti);

                    if (!entity.getAccessToken().equals(token)) {
                        log.warn("[DB] Token mismatch for jti={} -> DB token != Provided token", jti);
                        return Mono.error(new RuntimeException("Token does not match"));
                    }

                    log.debug("[DB] Token matches. Caching token for jti={}", jti);

                    tokenCache.put(jti, TokenDTO.builder()
                            .accessToken(entity.getAccessToken())
                            .appName(entity.getAppName())
                            .audience(entity.getAudience())
                            .id(entity.getId())
                            .createdAt(entity.getCreatedAt())
                            .expiresAt(entity.getExpiresAt())
                            .userId(entity.getUserId())
                            .build()
                    );

                    return Mono.just(claims);
                });
    }

    private boolean isPublicEndpoint(String path, String method) {
        boolean result = PUBLIC_ENDPOINTS.entrySet().stream()
                .anyMatch(entry -> path.endsWith(entry.getKey()) && entry.getValue().contains(method));

        log.debug("[PUBLIC CHECK] Path={} Method={} IsPublic={}", path, method, result);
        return result;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        log.warn("[UNAUTHORIZED] Returning 401: {}", message);

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
            log.error("[UNAUTHORIZED] Error serializing ApiError: {}", e.getMessage());
            bytes = ("{\"message\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        }

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }
}
