package com.celesoft.auth.service.impl;

import com.celesoft.auth.dto.LogInDTO;
import com.celesoft.auth.dto.LogInResponseDTO;
import com.celesoft.auth.dto.UserSecurityDTO;
import com.celesoft.auth.mapper.UserAuthMapper;
import com.celesoft.auth.repository.TokenAuthRepository;
import com.celesoft.auth.repository.UserAuthRepository;
import com.celesoft.auth.service.AuthService;
import com.celesoft.entities.security.TokenEntity;
import com.celesoft.utils.Helpers;
import com.celesoft.utils.JwtUtil;
import com.celesoft.utils.enums.ApplicationEnum;
import com.celesoft.utils.enums.AudienceEnum;
import com.celesoft.utils.enums.UserStatusEnum;
import com.celesoft.utils.dto.SecurityOptionsDTO;
import com.celesoft.utils.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final UserAuthRepository userRepository;
    private final TokenAuthRepository tokenRepository;
    private final UserAuthMapper userMapper;
    private static final int MAX_ATTEMPTS = 3;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(10);


    @Override
    public Mono<LogInResponseDTO> logIn(LogInDTO request) {
        if (AudienceEnum.fromCode(request.getAudience()) == null) {
            return Mono.error(new BusinessException("El valor de 'audience' es inválido", HttpStatus.BAD_REQUEST));
        }
        if (ApplicationEnum.fromCode(request.getAppName()) == null) {
            return Mono.error(new BusinessException("El valor de 'appName' es inválido", HttpStatus.BAD_REQUEST));
        }
        OffsetDateTime now = OffsetDateTime.now();
        return userRepository.findByUsername(request.getUsername())
                .switchIfEmpty(Mono.error(new BusinessException("Usuario o contraseña inválidos", HttpStatus.UNAUTHORIZED)))
                .map(userMapper::toDto)
                .flatMap(user -> {
                    if (Objects.equals(UserStatusEnum.DELETED.getId(), user.getStatusId())) {
                        return Mono.error(new BusinessException("Tu cuenta ha sido eliminada. Contacta soporte.", HttpStatus.FORBIDDEN));
                    }
                    if (user.getSecurity() == null) {
                        user.setSecurity(new UserSecurityDTO());
                    }
                    UserSecurityDTO sec = user.getSecurity();
                    if (sec.getLockedUntil() != null && sec.getLockedUntil().isAfter(now)) {
                        return Mono.error(new BusinessException("Cuenta bloqueada hasta " + sec.getLockedUntil(), HttpStatus.FORBIDDEN));
                    }
                    return Mono.just(user);
                })
                .flatMap(user -> {
                    boolean invalidPassword = !Objects.equals(request.getPassword(), user.getPassword());
                    UserSecurityDTO sec = user.getSecurity();
                    if (invalidPassword) {
                        int attempts = Optional.ofNullable(sec.getLoginAttempts()).orElse(0) + 1;
                        sec.setLoginAttempts(attempts);
                        if (attempts >= MAX_ATTEMPTS) {
                            sec.setLockedUntil(now.plus(LOCK_DURATION));
                            user.setPreviousStatusId(user.getStatusId());
                            user.setStatusId(UserStatusEnum.LOCKED.getId());
                        }
                        return userRepository.save(userMapper.toEntity(user))
                                .then(Mono.error(new BusinessException("Usuario o contraseña inválidos", HttpStatus.UNAUTHORIZED)));
                    }
                    sec.setLoginAttempts(0);
                    sec.setLockedUntil(null);
                    sec.setLastLoginAt(now);
                    if (UserStatusEnum.LOCKED.getId().equals(user.getStatusId()) && user.getPreviousStatusId() != null) {
                        user.setStatusId(user.getPreviousStatusId());
                        user.setPreviousStatusId(null);
                    }
                    return Mono.just(user);
                })
                .flatMap(user -> userRepository.save(userMapper.toEntity(user))
                        .then(tokenRepository.deleteByUserIdAndAudienceAndAppName(user.getId(), request.getAudience(), request.getAppName()))
                        .then(Mono.defer(() -> {
                            Long tokenId = Helpers.generateUniqueNumberDataBase();
                            String accessToken = JwtUtil.generateToken(
                                    user.getUsername(),
                                    String.valueOf(Helpers.generateUniqueNumberDataBase()),
                                    user.getId(),
                                    user.getSecurity().getRole(),
                                    request.getAudience(),
                                    request.getAppName()
                            );
                            TokenEntity token = TokenEntity.builder()
                                    .id(tokenId)
                                    .userId(user.getId())
                                    .audience(request.getAudience())
                                    .appName(request.getAppName())
                                    .accessToken(accessToken)
                                    .createdAt(now)
                                    .expiresAt(now.plusHours(2))
                                    .build();
                            return tokenRepository.save(token)
                                    .thenReturn(LogInResponseDTO.builder()
                                                    .accessToken(accessToken)
                                                    .build()
                                    );
                        }))
                );
    }

    @Override
    public Mono<String> logout(SecurityOptionsDTO options) {
        return tokenRepository.findById(options.getTokenId())
                .switchIfEmpty(Mono.error(new BusinessException(
                        "Token no encontrado o ya cerrado",
                        HttpStatus.NOT_FOUND
                )))
                .flatMap(foundToken ->
                        tokenRepository.delete(foundToken)
                                .thenReturn("Sesión cerrada correctamente.")
                )
                .onErrorResume(e -> {
                    if (e instanceof BusinessException) {
                        log.warn("[Logout] Error controlado: {}", e.getMessage());
                        return Mono.error(e);
                    }
                    log.error("[Logout] Error inesperado:", e);
                    return Mono.error(new BusinessException(
                            "Error al procesar el cierre de sesión",
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
                });
    }
}
