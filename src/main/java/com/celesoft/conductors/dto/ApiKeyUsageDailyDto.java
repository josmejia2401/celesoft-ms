package com.celesoft.conductors.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKeyUsageDailyDto {
    private Long id;
    private Long apiKeyId;
    private LocalDate day;
    private String channel;
    private Long count;
    private OffsetDateTime lastUpdated;
}

