package com.jac.users.controller;

import com.jac.users.dto.UpdatePasswordDTO;
import com.jac.users.dto.UpdatePasswordResDTO;
import com.jac.users.dto.UserDTO;
import com.jac.users.service.UserService;
import com.jac.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserDTO> getById(@PathVariable Long id, ServerHttpRequest request) {
        //ServerWebExchange exchange; Claims claims = exchange.getAttribute("jwtClaims");
        return JwtUtil.buildSecurityOptions(request).flatMap(options -> service.findById(id, options));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserDTO> create(@Valid @RequestBody UserDTO dto) {
        return service.save(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO dto, ServerHttpRequest request) {
        return JwtUtil.buildSecurityOptions(request).flatMap(options -> service.update(id, dto, options));
    }

    @PutMapping(value = "/{id}/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UpdatePasswordResDTO> updatePassword(@PathVariable Long id, @Valid @RequestBody UpdatePasswordDTO dto, ServerHttpRequest request) {
        return JwtUtil.buildSecurityOptions(request).flatMap(options -> service.updatePassword(id, dto, options));
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable Long id, ServerHttpRequest request) {
        return JwtUtil.buildSecurityOptions(request).flatMap(options -> service.delete(id, options));
    }
}
