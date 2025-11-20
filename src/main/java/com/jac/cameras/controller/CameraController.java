package com.jac.cameras.controller;


import com.jac.cameras.dto.CameraCreateDTO;
import com.jac.cameras.dto.CameraResponseDTO;
import com.jac.cameras.dto.CameraUpdateDTO;
import com.jac.cameras.service.CameraService;
import com.jac.utils.enums.camera.CameraStatusEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cameras")
public class CameraController {

    private final CameraService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CameraResponseDTO> create(@Valid @RequestBody CameraCreateDTO dto) {
        return service.create(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CameraResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CameraUpdateDTO dto) {
        return service.update(id, dto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<CameraResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CameraResponseDTO> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<CameraResponseDTO> findByStatus(@PathVariable CameraStatusEnum status) {
        return service.findByStatus(status);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}

