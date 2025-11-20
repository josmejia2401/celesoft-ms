package com.celesoft.entities.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_status")
public class UserStatusEntity {
    @Id
    private Long id;
    private String code;
    private String description;
}