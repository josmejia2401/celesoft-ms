package com.celesoft.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * DTO que representa la información de seguridad asociada a un usuario.
 * Incluye control de intentos de acceso, bloqueo temporal, último acceso y rol.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurityDTO {
    private Integer loginAttempts;
    private OffsetDateTime lockedUntil;
    private OffsetDateTime lastLoginAt;
    private String role;
}
