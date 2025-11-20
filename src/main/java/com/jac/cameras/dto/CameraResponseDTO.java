package com.jac.cameras.dto;

import com.jac.utils.enums.camera.CameraStatusEnum;

public record CameraResponseDTO(
        String id,
        String name,
        String rtspUrl,
        String location,
        CameraStatusEnum status,
        boolean ingestEnabled,
        String ingestNode
) {}
