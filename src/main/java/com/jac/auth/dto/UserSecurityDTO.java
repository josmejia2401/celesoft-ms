package com.jac.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurityDTO {
    private Integer loginAttempts;
    private Instant lockedUntil;
    private Instant lastLoginAt;
    private Set<String> roles;
}
