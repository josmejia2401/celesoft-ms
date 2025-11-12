package com.celesoft.users.service;

import com.celesoft.users.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<UserDTO> findAll();
    Mono<UserDTO> findById(Long id);
    Mono<UserDTO> save(UserDTO user);
    Mono<UserDTO> update(Long id, UserDTO user);
    Mono<Void> delete(Long id);
}
