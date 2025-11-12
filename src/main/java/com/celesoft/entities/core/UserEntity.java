package com.celesoft.entities.core;

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
@Table(schema = DBConstants.DB_SCHEMA_CORE, name = "users")
public class UserEntity {

    @Id
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    private String email;

    @Column("phone_number")
    private String phoneNumber;

    private String username;
    private String password;
    private String security;

    @Column("status_id")
    private Long statusId;

    @Column("previous_status_id")
    private Long previousStatusId;

    @Column("created_at")
    private OffsetDateTime createdAt;
}
