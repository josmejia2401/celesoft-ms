package com.celesoft.entities.core;

import com.celesoft.utils.DBConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DBConstants.DB_SCHEMA_CORE, name = "user_status")
public class UserStatusEntity {
    @Id
    private Long id;
    private String code;
    private String description;
}