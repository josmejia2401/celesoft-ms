package com.jac.users.controller;

import com.jac.users.dto.UserDTO;
import com.jac.users.service.UserService;
import com.jac.utils.exceptions.BusinessException;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserDTO> getById(@PathVariable Long id, ServerWebExchange exchange) {
        Claims claims = exchange.getAttribute("jwtClaims");
        if (claims == null) {
            return Mono.error(new BusinessException("Token no presente o inválido", HttpStatus.UNAUTHORIZED));
        }
        Object userIdClaim = claims.get("userId");
        if (userIdClaim == null) {
            return Mono.error(new BusinessException("Token inválido: no contiene userId", HttpStatus.UNAUTHORIZED));
        }
        long userIdFromToken;
        try {
            userIdFromToken = Long.parseLong(String.valueOf(userIdClaim));
        } catch (NumberFormatException e) {
            return Mono.error(new BusinessException("Formato inválido de userId en el token", HttpStatus.UNAUTHORIZED));
        }
        if (userIdFromToken != id) {
            return Mono.error(new BusinessException("No tienes permiso para acceder a este recurso", HttpStatus.FORBIDDEN));
        }
        return service.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserDTO> create(@Valid @RequestBody UserDTO dto) {
        return service.save(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
