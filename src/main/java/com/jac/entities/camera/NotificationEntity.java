package com.jac.entities.camera;

import com.jac.utils.enums.camera.NotificationChannelEnum;
import com.jac.utils.enums.camera.NotificationStatusEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document("notifications")
public record NotificationEntity(
        @Id Long id,
        Long userId,
        Long eventId,
        NotificationChannelEnum channel,
        Instant createdAt,
        NotificationStatusEnum status,
        Map<String, Object> payload
) {}
