package com.jac.cameras.dto;

import com.jac.utils.enums.camera.NotificationChannelEnum;
import com.jac.utils.enums.camera.NotificationStatusEnum;

import java.time.Instant;
import java.util.Map;

public record NotificationResponseDTO(
        String id,
        String userId,
        String eventId,
        NotificationChannelEnum channel,
        Instant createdAt,
        NotificationStatusEnum status,
        Map<String, Object> payload
) {}
