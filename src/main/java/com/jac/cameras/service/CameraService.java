package com.jac.cameras.service;

import com.jac.cameras.dto.CameraCreateDTO;
import com.jac.cameras.dto.CameraResponseDTO;
import com.jac.cameras.dto.CameraUpdateDTO;
import com.jac.utils.enums.camera.CameraStatusEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CameraService {

    Mono<CameraResponseDTO> create(CameraCreateDTO dto);

    Mono<CameraResponseDTO> update(Long id, CameraUpdateDTO dto);

    Flux<CameraResponseDTO> findAll();

    Mono<CameraResponseDTO> findById(Long id);

    Flux<CameraResponseDTO> findByStatus(CameraStatusEnum status);

    Mono<Void> delete(Long id);
}
