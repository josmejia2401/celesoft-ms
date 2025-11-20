package com.celesoft.entities.security;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

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
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;
}
