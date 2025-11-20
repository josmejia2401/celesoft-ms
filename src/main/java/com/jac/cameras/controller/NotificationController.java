package com.jac.cameras.controller;


import com.jac.cameras.dto.NotificationCreateDTO;
import com.jac.cameras.dto.NotificationResponseDTO;
import com.jac.cameras.service.NotificationService;
import com.jac.utils.enums.camera.NotificationStatusEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NotificationResponseDTO> create(@Valid @RequestBody NotificationCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NotificationResponseDTO> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<NotificationResponseDTO> findByUser(@PathVariable Long userId) {
        return service.findByUser(userId);
    }

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<NotificationResponseDTO> findByStatus(@PathVariable NotificationStatusEnum status) {
        return service.findByStatus(status);
    }
}

