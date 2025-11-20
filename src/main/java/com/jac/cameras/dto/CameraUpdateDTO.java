package com.jac.cameras.dto;


import com.jac.utils.enums.camera.CameraStatusEnum;

public record CameraUpdateDTO(
        String name,
        String rtspUrl,
        String location,
        CameraStatusEnum status,
        Boolean ingestEnabled
) {}
