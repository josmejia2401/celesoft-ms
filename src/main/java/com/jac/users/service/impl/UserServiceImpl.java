package com.jac.users.service.impl;

import com.jac.entities.security.UserEntity;
import com.jac.users.dto.UpdatePasswordDTO;
import com.jac.users.dto.UpdatePasswordResDTO;
import com.jac.users.dto.UserDTO;
import com.jac.users.dto.UserSecurityDTO;
import com.jac.users.mapper.UserMapper;
import com.jac.users.repository.TokenRepository;
import com.jac.users.repository.UserRepository;
import com.jac.users.service.UserService;
import com.jac.utils.Helpers;
import com.jac.utils.dto.SecurityOptionsDTO;
import com.jac.utils.enums.UserRolesEnum;
import com.jac.utils.enums.UserStatusEnum;
import com.jac.utils.exceptions.BusinessException;
import com.jac.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final TokenRepository tokenRepository;

    @Override
    public Mono<UserDTO> findById(Long id, SecurityOptionsDTO options) {
        if (!id.equals(options.getUserId())) {
            return Mono.error(new BusinessException("No autorizado para consultar este usuario", HttpStatus.FORBIDDEN));
        }
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

                    Set<String> roles = dto.getSecurity() != null
                            ? new HashSet<>(dto.getSecurity().getRoles())
                            : new HashSet<>();

                    if (roles.isEmpty()) {
                        roles.add(UserRolesEnum.USER.code());
                    }

                    for (String role : roles) {
                        if (UserRolesEnum.fromCode(role) == null) {
                            return Mono.error(new IllegalArgumentException("Invalid role: " + role));
                        }
                    }

                    dto.setId(Helpers.generateUniqueNumberDataBase());
                    dto.setStatusId(UserStatusEnum.PENDING.getId());
                    dto.setUsername(username);
                    dto.setSecurity(UserSecurityDTO
                            .builder()
                            .roles(roles)
                            .build());
                    UserEntity entity = mapper.toEntity(dto);
                    log.info("Saving new user id={} username={}", dto.getId(), dto.getUsername());
                    return repository.save(entity)
                            .map(mapper::toDto);
                });
    }

    @Override
    public Mono<UserDTO> update(Long id, UserDTO dto, SecurityOptionsDTO options) {
        if (!id.equals(options.getUserId())) {
            return Mono.error(new BusinessException("No autorizado para actualizar este usuario", HttpStatus.FORBIDDEN));
        }
        Set<String> roles = dto.getSecurity() != null
                ? new HashSet<>(dto.getSecurity().getRoles())
                : new HashSet<>();
        if (roles.isEmpty()) {
            roles.add(UserRolesEnum.USER.code());
        }
        for (String role : roles) {
            if (UserRolesEnum.fromCode(role) == null) {
                return Mono.error(new IllegalArgumentException("Invalid role: " + role));
            }
        }
        if (dto.getSecurity() == null) {
            dto.setSecurity(new UserSecurityDTO());
        }
        dto.getSecurity().setRoles(roles);
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado")))
                .flatMap(existing -> {
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<UpdatePasswordResDTO> updatePassword(Long id, UpdatePasswordDTO dto, SecurityOptionsDTO options) {
        if (!id.equals(options.getUserId())) {
            return Mono.error(new BusinessException("No autorizado para actualizar contraseña de este usuario", HttpStatus.FORBIDDEN));
        }
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado")))
                .flatMap(existing -> {
                    if (!existing.getPassword().equals(dto.getPassword())) {
                        return Mono.error(new BusinessException("La contraseña actual no es correcta", HttpStatus.FORBIDDEN));
                    }
                    if (Objects.equals(dto.getNewPassword(), existing.getPassword())) {
                        return Mono.error(new BusinessException(
                                "La nueva contraseña no puede ser igual a la anterior",
                                HttpStatus.BAD_REQUEST
                        ));
                    }
                    existing.setPassword(dto.getNewPassword());
                    return repository.save(existing);
                })
                .map(p -> UpdatePasswordResDTO
                        .builder()
                        .message("Contraseña actualizada correctamente")
                        .build());
    }

    @Override
    public Mono<Void> delete(Long id, SecurityOptionsDTO options) {
        if (!id.equals(options.getUserId())) {
            return Mono.error(new BusinessException("No autorizado para eliminar este usuario", HttpStatus.FORBIDDEN));
        }
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado")))
                .flatMap(existing -> {
                    existing.setStatusId(UserStatusEnum.DELETED.getId());
                    return repository.save(existing);
                })
                .then(Mono.defer(() -> tokenRepository.deleteByUserId(id)))
                .then();
    }
}
