package com.celesoft.entities.notifications;

import com.celesoft.utils.DBConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DBConstants.DB_SCHEMA_CORE, name = "notifications")
public class NotificationEntity {

    @Id
    private Long id;

    @Column("api_key_id")
    private Long apiKey;

    private String channel;

    private String payload;

    @Column("status_id")
    private Long status;

    private Short attempts = 0;

    private Short priority = 50;

    @Column("provider_id")
    private String providerId;

    private String error;

    @Column("created_at")
    private OffsetDateTime createdAt;
}

