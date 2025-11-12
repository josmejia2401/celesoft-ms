package com.celesoft.auth.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TokenDTO {
    private Long id;
    private Long userId;
    private String accessToken;
    private String appName;
    private String audience;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;
}
