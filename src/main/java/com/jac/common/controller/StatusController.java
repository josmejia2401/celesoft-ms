package com.jac.common.controller;

import com.jac.common.dto.StatusDTO;
import com.jac.common.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/common/status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService service;

    @GetMapping("/{id}")
    public Mono<StatusDTO> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/category/{category}")
    public Flux<StatusDTO> findByCategory(@PathVariable String category) {
        return service.findByCategory(category);
    }

    @GetMapping
    public Flux<StatusDTO> findAll() {
        return service.findAll();
    }
}

