package com.jac.entities.camera;

import com.jac.utils.enums.camera.CameraStatusEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cameras")
public record CameraEntity(
        @Id Long id,
        String name,
        String rtspUrl,
        String location,
        CameraStatusEnum status,
        boolean ingestEnabled,
        String ingestNode
) {}
