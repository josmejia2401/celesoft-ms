package com.celesoft.auth.service;

import com.celesoft.auth.dto.LogInDTO;
import com.celesoft.auth.dto.LogInResponseDTO;
import com.celesoft.utils.dto.SecurityOptionsDTO;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<LogInResponseDTO> logIn(LogInDTO request);
    Mono<String> logout(SecurityOptionsDTO options);
}
