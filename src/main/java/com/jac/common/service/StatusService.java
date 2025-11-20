package com.jac.common.service;

import com.jac.common.dto.StatusDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StatusService {
    Flux<StatusDTO> findByCategory(String category);
    Mono<StatusDTO> findById(Long id);
    Flux<StatusDTO> findAll();
}

