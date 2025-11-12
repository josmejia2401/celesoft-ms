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
@Table(schema = DBConstants.DB_SCHEMA_CORE, name = "api_key_usage_daily")
public class ApiKeyUsageDailyEntity {

    @Id
    private Long id;

    @Column("api_key_id")
    private Long apiKey;

    private LocalDate day;

    private String channel;

    private Long count = 0L;

    @Column("last_updated")
    private OffsetDateTime lastUpdated;
}

