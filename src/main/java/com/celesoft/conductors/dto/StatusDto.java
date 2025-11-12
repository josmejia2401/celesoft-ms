package com.celesoft.conductors.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusDto {
    private Long id;
    private String category;
    private String code;
    private String name;
    private String description;
    private Boolean isActive;
    private OffsetDateTime createdAt;
}

