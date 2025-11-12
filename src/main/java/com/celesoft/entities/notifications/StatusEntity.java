package com.celesoft.entities.notifications;

import com.celesoft.utils.DBConstants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DBConstants.DB_SCHEMA_CORE, name = "status")
public class StatusEntity {
    @Id
    private Long id;
    // p.ej. 'notification', 'api_key', 'user', 'system'
    private String category;
    private String code;
    private String name;
    private String description;
    @Column("is_active")
    private Boolean isActive = true;
    @Column("created_at")
    private OffsetDateTime createdAt;
}

