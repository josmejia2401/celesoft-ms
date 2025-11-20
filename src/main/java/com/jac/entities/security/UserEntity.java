package com.celesoft.entities.security;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserEntity {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    @Indexed(unique = true)
    private String username;
    private String password;
    private UserSecurityEntity security;
    private Long statusId;
    private Long previousStatusId;
    private OffsetDateTime createdAt;
}
