package com.jac.cameras.controller;

import com.jac.cameras.dto.EventRequestDTO;
import com.jac.cameras.dto.EventResponseDTO;
import com.jac.cameras.service.EventService;
import com.jac.utils.enums.camera.EventTypeEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<EventResponseDTO> create(@Valid @RequestBody EventRequestDTO dto) {
        return service.createEvent(dto);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<EventResponseDTO> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping(value = "/camera/{cameraId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<EventResponseDTO> findByCamera(@PathVariable Long cameraId) {
        return service.findByCamera(cameraId);
    }

    @GetMapping(value = "/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<EventResponseDTO> findByType(@PathVariable EventTypeEnum type) {
        return service.findByType(type);
    }
}

