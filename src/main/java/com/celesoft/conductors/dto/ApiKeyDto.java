package com.celesoft.conductors.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKeyDto {
    private Long id;
    private String name;
    private Long userId;
    private String secretHash;
    private String category;
    private Long statusId;
    private Integer dailyLimit;
    private Long monthlyLimit;
    private Integer ratePerMin;
    private String metadata; // JSON string
    private OffsetDateTime createdAt;
}
