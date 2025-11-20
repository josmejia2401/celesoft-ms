package com.jac.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
    private Instant lockedUntil;
    private Instant lastLoginAt;
    private String role;
}
