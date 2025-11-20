package com.jac.cameras.service;


import com.jac.cameras.dto.EventRequestDTO;
import com.jac.cameras.dto.EventResponseDTO;
import com.jac.utils.enums.camera.EventTypeEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {

    Mono<EventResponseDTO> createEvent(EventRequestDTO dto);

    Flux<EventResponseDTO> findByCamera(Long cameraId);

    Flux<EventResponseDTO> findByType(EventTypeEnum type);

    Mono<EventResponseDTO> findById(Long id);
}