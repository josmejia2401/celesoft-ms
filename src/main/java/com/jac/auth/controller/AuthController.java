package com.jac.auth.controller;

import com.jac.auth.dto.LogInDTO;
import com.jac.auth.dto.LogInResponseDTO;
import com.jac.auth.service.AuthService;
import com.jac.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public Mono<LogInResponseDTO> login(@Valid @RequestBody LogInDTO dto, @RequestHeader(value = "x-app-name", required = true) String appName, @RequestHeader(value = "x-audience", required = true) String audience) {
        dto.setAppName(appName.trim());
        dto.setAudience(audience.trim());
        return service.logIn(dto);
    }

    @PostMapping("/logout")
    public Mono<String> logout(ServerHttpRequest request) {
        return JwtUtil.buildSecurityOptions(request).flatMap(service::logout);
    }
}
