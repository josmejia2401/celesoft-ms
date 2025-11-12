package com.celesoft.entities.core;

import java.time.LocalDateTime;
import java.util.UUID;

import com.celesoft.utils.DBConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DBConstants.DB_SCHEMA_CORE, name = "account_activations")
public class AccountActivationEntity {

    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    private String token;

    @Column("expires_at")
    private LocalDateTime expiresAt;

    @Column("created_at")
    private LocalDateTime createdAt;
}
