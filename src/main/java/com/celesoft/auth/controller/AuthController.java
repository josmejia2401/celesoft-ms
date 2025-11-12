package com.celesoft.auth.controller;

import com.celesoft.utils.dto.BaseDTO;
import com.celesoft.auth.dto.LogInDTO;
import com.celesoft.auth.dto.LogInResponseDTO;
import com.celesoft.auth.service.AuthService;
import com.celesoft.utils.exceptions.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public Mono<BaseDTO<LogInResponseDTO>> login(
            @Valid @RequestBody LogInDTO dto,
            @RequestHeader(value = "X-App-Name", required = false) String appName,
            @RequestHeader(value = "X-Audience", required = false) String audience
    ) {
        if (appName == null || appName.isBlank()) {
            throw new BusinessException("El header 'X-App-Name' es obligatorio", HttpStatus.BAD_REQUEST);
        }
        if (audience == null || audience.isBlank()) {
            throw new BusinessException("El header 'X-Audience' es obligatorio", HttpStatus.BAD_REQUEST);
        }
        dto.setAppName(appName.trim());
        dto.setAudience(audience.trim());
        return service.logIn(dto);
    }

    @PostMapping("/logout")
    public Mono<BaseDTO<String>> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Mono.error(new BusinessException("Token de autorización inválido", HttpStatus.UNAUTHORIZED));
        }
        return service.logout(authorization);
    }

}
