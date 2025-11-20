package com.jac.entities.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_security")
public class UserSecurityEntity {
    private Integer loginAttempts;
    private Instant lockedUntil;
    private Instant lastLoginAt;
    private String role;
}
