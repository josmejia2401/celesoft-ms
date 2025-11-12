package com.celesoft.entities.notifications;

import com.celesoft.utils.DBConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DBConstants.DB_SCHEMA_CORE, name = "api_keys")
public class ApiKeyEntity {

    @Id
    private Long id;
    @Column("user_id")
    private Long userId;

    private String name;

    @Column("secret_hash")
    private String secretHash;

    private String category;

    @Column("status_id")
    private Long statusId;

    @Column("daily_limit")
    private Integer dailyLimit;

    @Column("monthly_limit")
    private Long monthlyLimit;

    @Column("rate_per_min")
    private Integer ratePerMin;

    private String metadata;

    @Column("created_at")
    private OffsetDateTime createdAt;
}

