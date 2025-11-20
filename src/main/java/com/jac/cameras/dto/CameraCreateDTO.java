package com.jac.cameras.dto;

import com.jac.utils.enums.camera.CameraStatusEnum;

public record CameraCreateDTO(
        String name,
        CameraStatusEnum status,
        String rtspUrl,
        String location
) {}
