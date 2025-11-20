package com.jac.common.service.impl;

import com.jac.common.dto.StatusDTO;
import com.jac.common.mapper.StatusMapper;
import com.jac.common.service.StatusService;
import com.jac.utils.enums.StatusEnum;
import com.jac.utils.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusMapper statusMapper;

    @Override
    public Flux<StatusDTO> findByCategory(String category) {
        return Flux.fromArray(StatusEnum.values())
                .filter(status -> status.getCategory().equalsIgnoreCase(category))
                .map(statusMapper::toDto);
    }


    @Override
    public Mono<StatusDTO> findById(Long id) {
        return Mono.justOrEmpty(StatusEnum.fromId(id))
                .switchIfEmpty(Mono.error(new BusinessException("Estado no encontrado", HttpStatus.NOT_FOUND)))
                .map(statusMapper::toDto);
    }


    @Override
    public Flux<StatusDTO> findAll() {
        return Flux.fromArray(StatusEnum.values())
                .map(statusMapper::toDto);
    }
}

