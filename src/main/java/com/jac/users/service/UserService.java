package com.jac.users.service;

import com.jac.users.dto.UpdatePasswordDTO;
import com.jac.users.dto.UpdatePasswordResDTO;
import com.jac.users.dto.UserDTO;
import com.jac.utils.dto.SecurityOptionsDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDTO> findById(Long id, SecurityOptionsDTO options);
    Mono<UserDTO> save(UserDTO user);
    Mono<UserDTO> update(Long id, UserDTO user, SecurityOptionsDTO options);
    Mono<UpdatePasswordResDTO> updatePassword(Long id, UpdatePasswordDTO user, SecurityOptionsDTO options);
    Mono<Void> delete(Long id, SecurityOptionsDTO options);
}
