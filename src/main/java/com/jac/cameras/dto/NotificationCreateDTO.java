package com.jac.cameras.dto;

import com.jac.utils.enums.camera.NotificationChannelEnum;

import java.util.Map;

public record NotificationCreateDTO(
        String userId,
        String eventId,
        NotificationChannelEnum channel,
        Map<String, Object> payload
) {}
