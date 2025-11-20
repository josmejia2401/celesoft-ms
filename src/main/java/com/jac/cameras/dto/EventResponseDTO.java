package com.jac.cameras.dto;

import com.jac.utils.enums.camera.EventTypeEnum;

import java.time.Instant;
import java.util.Map;

public record EventResponseDTO(
        String id,
        String cameraId,
        EventTypeEnum type,
        Instant timestamp,
        Map<String, Object> payload,
        String sourceStreamId
) {}
