package com.celesoft.entities.core;

import java.time.OffsetDateTime;

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
@Table(schema = DBConstants.DB_SCHEMA_CORE, name = "tokens")
public class TokenEntity {

    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("access_token")
    private String accessToken;

    @Column("app_name")
    private String appName;

    private String audience;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("expires_at")
    private OffsetDateTime expiresAt;
}
