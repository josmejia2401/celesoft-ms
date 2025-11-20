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
public class TokenDTO {
    private Long id;
    private Long userId;
    private String accessToken;
    private String appName;
    private String audience;
    private Instant createdAt;
    private Instant expiresAt;
}
