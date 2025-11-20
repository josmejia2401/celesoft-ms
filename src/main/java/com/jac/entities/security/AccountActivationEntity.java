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
@Document(collection = "account_activations")
public class AccountActivationEntity {
    @Id
    private Long id;
    private Long userId;
    private String token;
    private Instant expiresAt;
    private Instant createdAt;
}
