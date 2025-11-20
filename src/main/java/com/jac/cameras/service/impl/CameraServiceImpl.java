package com.jac.cameras.service.impl;


import com.jac.cameras.dto.CameraCreateDTO;
import com.jac.cameras.dto.CameraResponseDTO;
import com.jac.cameras.dto.CameraUpdateDTO;
import com.jac.cameras.mapper.CameraMapper;
import com.jac.cameras.repository.CameraRepository;
import com.jac.cameras.service.CameraService;
import com.jac.entities.camera.CameraEntity;
import com.jac.utils.enums.camera.CameraStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CameraServiceImpl implements CameraService {

    private final CameraRepository repository;
    private final CameraMapper mapper;

    @Override
    public Mono<CameraResponseDTO> create(CameraCreateDTO dto) {
        CameraEntity entity = mapper.toEntity(dto);
        return repository.save(entity)
                .map(mapper::toDto);
    }

    @Override
    public Mono<CameraResponseDTO> update(Long id, CameraUpdateDTO dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Camera not found")))
                .flatMap(camera -> {
                    CameraEntity updated = mapper.update(dto, camera);
                    return repository.save(updated);
                })
                .map(mapper::toDto);
    }

    @Override
    public Flux<CameraResponseDTO> findAll() {
        return repository.findAll()
                .map(mapper::toDto);
    }

    @Override
    public Mono<CameraResponseDTO> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }

    @Override
    public Flux<CameraResponseDTO> findByStatus(CameraStatusEnum status) {
        return repository.findByStatus(status)
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}

