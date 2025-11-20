package com.jac.entities.camera;

import com.jac.utils.enums.camera.EventTypeEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document("events")
public record EventEntity(
        @Id Long id,
        Long cameraId,
        EventTypeEnum type,
        Instant timestamp,
        Map<String, Object> payload,
        String sourceStreamId
) {}
