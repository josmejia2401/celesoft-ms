package com.celesoft.auth.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasswordResetDTO {
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
