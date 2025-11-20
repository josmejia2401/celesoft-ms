package com.jac.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDTO {
    private Long id;
    private Long userId;
    private String token;
    private Instant expiresAt;
    private Instant createdAt;
}
