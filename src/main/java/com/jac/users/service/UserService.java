package com.jac.users.service;

import com.jac.users.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDTO> findById(Long id);
    Mono<UserDTO> save(UserDTO user);
    Mono<UserDTO> update(Long id, UserDTO user);
    Mono<Void> delete(Long id);
}
