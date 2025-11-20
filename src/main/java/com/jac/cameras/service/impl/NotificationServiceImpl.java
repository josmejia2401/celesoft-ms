package com.jac.cameras.service.impl;

import com.jac.cameras.dto.NotificationCreateDTO;
import com.jac.cameras.dto.NotificationResponseDTO;
import com.jac.cameras.mapper.NotificationMapper;
import com.jac.cameras.repository.NotificationRepository;
import com.jac.cameras.service.NotificationService;
import com.jac.entities.camera.NotificationEntity;
import com.jac.utils.enums.camera.NotificationStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    @Override
    public Mono<NotificationResponseDTO> create(NotificationCreateDTO dto) {
        NotificationEntity entity = mapper.toEntity(dto);
        return repository.save(entity)
                .map(mapper::toDto);
    }

    @Override
    public Flux<NotificationResponseDTO> findByUser(Long userId) {
        return repository.findByUserId(userId)
                .map(mapper::toDto);
    }

    @Override
    public Flux<NotificationResponseDTO> findByStatus(NotificationStatusEnum status) {
        return repository.findByStatus(status)
                .map(mapper::toDto);
    }

    @Override
    public Mono<NotificationResponseDTO> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }
}
