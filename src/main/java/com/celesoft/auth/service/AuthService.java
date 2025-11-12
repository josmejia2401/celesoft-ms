package com.celesoft.auth.service;

import com.celesoft.utils.dto.BaseDTO;
import com.celesoft.auth.dto.LogInDTO;
import com.celesoft.auth.dto.LogInResponseDTO;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<BaseDTO<LogInResponseDTO>> logIn(LogInDTO request);
    Mono<BaseDTO<String>> logout(String authorizationHeader);
}
