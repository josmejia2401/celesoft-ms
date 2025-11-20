package com.jac.auth.controller;

import com.jac.auth.dto.LogInDTO;
import com.jac.auth.dto.LogInResponseDTO;
import com.jac.auth.service.AuthService;
import com.jac.utils.JwtUtil;
import com.jac.utils.exceptions.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<LogInResponseDTO> login(
            @Valid @RequestBody LogInDTO dto,
            @RequestHeader(value = "x-app-name", required = true) String appName,
            @RequestHeader(value = "x-audience", required = true) String audience
    ) {
        if (appName == null || appName.isBlank()) {
            return Mono.error(new BusinessException("El header 'X-App-Name' es obligatorio", HttpStatus.BAD_REQUEST));
        }
        if (audience == null || audience.isBlank()) {
            return Mono.error(new BusinessException("El header 'X-Audience' es obligatorio", HttpStatus.BAD_REQUEST));
        }
        dto.setAppName(appName.trim());
        dto.setAudience(audience.trim());
        return service.logIn(dto);
    }

    @PostMapping("/logout")
    public Mono<String> logout(ServerHttpRequest request) {
        return JwtUtil.buildSecurityOptions(request).flatMap(service::logout);
    }
}
