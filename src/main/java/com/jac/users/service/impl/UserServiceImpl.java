package com.jac.users.service.impl;

import com.jac.entities.security.UserEntity;
import com.jac.users.dto.UserDTO;
import com.jac.users.mapper.UserMapper;
import com.jac.users.repository.UserRepository;
import com.jac.users.service.UserService;
import com.jac.utils.Helpers;
import com.jac.utils.enums.UserStatusEnum;
import com.jac.utils.exceptions.BusinessException;
import com.jac.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public Mono<UserDTO> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<UserDTO> save(UserDTO dto) {
        String username = dto.getUsername().trim();
        return repository.existsByUsername(username)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(
                                "El usuario ya existe",
                                HttpStatus.CONFLICT
                        ));
                    }
                    dto.setId(Helpers.generateUniqueNumberDataBase());
                    dto.setStatusId(UserStatusEnum.PENDING.getId());
                    dto.setUsername(username);
                    UserEntity entity = mapper.toEntity(dto);
                    log.info("Saving new user id={} username={}", dto.getId(), dto.getUsername());
                    return repository.save(entity)
                            .map(mapper::toDto);
                });
    }




    @Override
    public Mono<UserDTO> update(Long id, UserDTO dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado")))
                .flatMap(existing -> {
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado")))
                .flatMap(existing -> {
                    existing.setStatusId(UserStatusEnum.DELETED.getId());
                    // TODO: Eliminar todos los tokens asociados al usuario
                    return repository.save(existing);
                })
                .then();
    }
}
