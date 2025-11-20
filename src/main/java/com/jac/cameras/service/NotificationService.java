package com.jac.cameras.service;

import com.jac.cameras.dto.NotificationCreateDTO;
import com.jac.cameras.dto.NotificationResponseDTO;
import com.jac.utils.enums.camera.NotificationStatusEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<NotificationResponseDTO> create(NotificationCreateDTO dto);

    Flux<NotificationResponseDTO> findByUser(Long userId);

    Flux<NotificationResponseDTO> findByStatus(NotificationStatusEnum status);

    Mono<NotificationResponseDTO> findById(Long id);
}
