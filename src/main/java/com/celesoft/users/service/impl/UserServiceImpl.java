package com.celesoft.users.service.impl;

import com.celesoft.entities.core.UserEntity;
import com.celesoft.users.dto.UserDTO;
import com.celesoft.users.mapper.UserMapper;
import com.celesoft.users.repository.UserRepository;
import com.celesoft.users.service.UserService;
import com.celesoft.utils.Helpers;
import com.celesoft.utils.UserStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final R2dbcEntityTemplate template;
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public Flux<UserDTO> findAll() {
        return repository.findAll().map(mapper::toDto);
    }

    @Override
    public Mono<UserDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public Mono<UserDTO> save(UserDTO dto) {
        dto.setId(Helpers.generateUniqueNumberDataBase());
        dto.setStatusId(UserStatusEnum.PENDING.getCode());
        UserEntity entity = mapper.toEntity(dto);

        if (entity.getId() == null) {
            return repository.save(entity).map(mapper::toDto);
        }

        return repository.existsById(entity.getId())
                .flatMap(exists -> {
                    if (exists) {
                        return repository.save(entity);
                    } else {
                        return template.insert(UserEntity.class).using(entity);
                    }
                }).map(mapper::toDto);
    }

    @Override
    public Mono<UserDTO> update(Long id, UserDTO dto) {
        return repository.findById(id)
                .flatMap(existing -> {
                    Long statusId = existing.getStatusId();
                    mapper.updateEntityFromDto(dto, existing);
                    existing.setStatusId(statusId);
                    return repository.save(existing);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}
