package com.celesoft.conductors.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private Long id;
    private Long apiKeyId;
    private String channel;
    private String payload; // JSON string
    private Long statusId;
    private Short attempts;
    private Short priority;
    private String providerId;
    private String error; // JSON string
    private OffsetDateTime createdAt;
}
