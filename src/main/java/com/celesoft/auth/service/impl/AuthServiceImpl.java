package com.celesoft.auth.service.impl;

import com.celesoft.utils.dto.BaseDTO;
import com.celesoft.auth.dto.LogInDTO;
import com.celesoft.auth.dto.LogInResponseDTO;
import com.celesoft.auth.dto.UserSecurityDTO;
import com.celesoft.auth.mapper.UserAuthMapper;
import com.celesoft.auth.repository.TokenAuthRepository;
import com.celesoft.auth.repository.UserAuthRepository;
import com.celesoft.auth.service.AuthService;
import com.celesoft.entities.core.TokenEntity;
import com.celesoft.utils.Helpers;
import com.celesoft.utils.JwtUtil;
import com.celesoft.utils.UserStatusEnum;
import com.celesoft.utils.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final UserAuthRepository userRepository;
    private final TokenAuthRepository tokenRepository;
    private final UserAuthMapper userMapper;
    private final JwtUtil jwtUtil;

    private static final int MAX_ATTEMPTS = 3;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(10);


    @Override
    public Mono<BaseDTO<LogInResponseDTO>> logIn(LogInDTO request) {
        return userRepository.findByUsername(request.getUsername())
                .switchIfEmpty(Mono.error(new BusinessException("Usuario o contraseña inválidos", HttpStatus.UNAUTHORIZED)))
                .flatMap(user -> Mono.just(userMapper.toDto(user)))
                .flatMap(user -> {
                    OffsetDateTime now = OffsetDateTime.now();
                    if (Objects.equals(UserStatusEnum.DELETED.getCode(), user.getStatusId())) {
                        return Mono.error(new BusinessException("Tu cuenta ha sido eliminada. Contacta soporte.", HttpStatus.FORBIDDEN));
                    }
                    if (user.getSecurity() == null) {
                        user.setSecurity(new UserSecurityDTO());
                    }
                    if (user.getSecurity().getLockedUntil() != null && user.getSecurity().getLockedUntil().isAfter(now)) {
                        return Mono.error(new BusinessException(
                                "Cuenta bloqueada hasta " + user.getSecurity().getLockedUntil(),
                                HttpStatus.FORBIDDEN));
                    }
                    if (!Objects.equals(request.getPassword(), user.getPassword())) {
                        int attempts = (user.getSecurity().getLoginAttempts() != null ? user.getSecurity().getLoginAttempts() : 0) + 1;
                        user.getSecurity().setLoginAttempts(attempts);
                        if (attempts >= MAX_ATTEMPTS) {
                            user.getSecurity().setLockedUntil(now.plus(LOCK_DURATION));
                            user.setPreviousStatusId(user.getStatusId());
                            user.setStatusId(UserStatusEnum.LOCKED.getCode());
                        }
                        return userRepository.save(userMapper.toEntity(user))
                                .then(Mono.error(new BusinessException("Usuario o contraseña inválidos", HttpStatus.UNAUTHORIZED)));
                    }
                    user.getSecurity().setLoginAttempts(0);
                    user.getSecurity().setLockedUntil(null);
                    user.getSecurity().setLastLoginAt(now);
                    if (UserStatusEnum.LOCKED.getCode().equals(user.getStatusId()) && user.getPreviousStatusId() != null) {
                        user.setStatusId(user.getPreviousStatusId());
                        user.setPreviousStatusId(null);
                    }
                    return Mono.just(user);
                })
                .flatMap(user -> {
                    OffsetDateTime now = OffsetDateTime.now();
                    return userRepository.save(userMapper.toEntity(user))
                            .then(tokenRepository.deleteByUserIdAndAudienceAndAppName(user.getId(), request.getAudience(), request.getAppName()))
                            .then(Mono.defer(() -> {
                                Long tokenId = Helpers.generateUniqueNumberDataBase();
                                String accessToken = jwtUtil.generateToken(
                                        user.getUsername(),
                                        String.valueOf(Helpers.generateUniqueNumberDataBase()),
                                        user.getId(),
                                        user.getSecurity().getRole(),
                                        request.getAudience(),
                                        request.getAppName());
                                TokenEntity token = TokenEntity
                                        .builder()
                                        .id(tokenId)
                                        .userId(user.getId())
                                        .audience(request.getAudience())
                                        .appName(request.getAppName())
                                        .accessToken(accessToken)
                                        .createdAt(now)
                                        .expiresAt(now.plusHours(2))
                                        .build();
                                return tokenRepository
                                        .save(token)
                                        .thenReturn(BaseDTO
                                                .ok(LogInResponseDTO
                                                        .builder()
                                                        .accessToken(accessToken)
                                                        .build()
                                        ));
                            }));
                });
    }

    @Override
    public Mono<BaseDTO<String>> logout(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return Mono.fromCallable(() -> jwtUtil.decodeToken(token))
                .flatMap(decoded -> {
                    Long tokenId = Long.valueOf(decoded.getId()); // jti del token
                    log.info("[Logout] Token decodificado correctamente. JTI: {}", tokenId);
                    return tokenRepository.findById(tokenId)
                            .flatMap(foundToken ->
                                    tokenRepository.delete(foundToken)
                                            .thenReturn(BaseDTO.ok("Sesión cerrada correctamente.", "Sesión cerrada correctamente."))
                            )
                            .switchIfEmpty(Mono.error(
                                    new BusinessException("Token no encontrado o ya cerrado", HttpStatus.NOT_FOUND)
                            ));
                })
                .onErrorResume(e -> {
                    log.error("[Logout] Error al cerrar sesión: {}", e.getMessage(), e);
                    return Mono.error(new BusinessException("Error al procesar el cierre de sesión", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
}
