package com.jac.auth.service;

import com.jac.auth.dto.LogInDTO;
import com.jac.auth.dto.LogInResponseDTO;
import com.jac.utils.dto.SecurityOptionsDTO;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<LogInResponseDTO> logIn(LogInDTO request);
    Mono<String> logout(SecurityOptionsDTO options);
}
