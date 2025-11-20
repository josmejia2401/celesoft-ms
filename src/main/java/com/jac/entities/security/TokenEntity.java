package com.jac.entities.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tokens")
public class TokenEntity {
    @Id
    private Long id;
    private Long userId;
    private String accessToken;
    private String appName;
    private String audience;
    private Instant createdAt;
    private Instant expiresAt;
}
