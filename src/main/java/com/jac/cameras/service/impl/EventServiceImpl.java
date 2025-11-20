package com.jac.cameras.service.impl;

import com.jac.cameras.dto.EventRequestDTO;
import com.jac.cameras.dto.EventResponseDTO;
import com.jac.cameras.mapper.EventMapper;
import com.jac.cameras.repository.EventRepository;
import com.jac.cameras.service.EventService;
import com.jac.entities.camera.EventEntity;
import com.jac.utils.enums.camera.EventTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final EventMapper mapper;

    @Override
    public Mono<EventResponseDTO> createEvent(EventRequestDTO dto) {
        EventEntity entity = mapper.toEntity(dto);
        return repository.save(entity)
                .map(mapper::toDto);
    }

    @Override
    public Flux<EventResponseDTO> findByCamera(Long cameraId) {
        return repository.findByCameraId(cameraId)
                .map(mapper::toDto);
    }

    @Override
    public Flux<EventResponseDTO> findByType(EventTypeEnum type) {
        return repository.findByType(type)
                .map(mapper::toDto);
    }

    @Override
    public Mono<EventResponseDTO> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }
}

