package com.jac.cameras.mapper;

import com.jac.cameras.dto.CameraCreateDTO;
import com.jac.cameras.dto.CameraResponseDTO;
import com.jac.cameras.dto.CameraUpdateDTO;
import com.jac.entities.camera.CameraEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CameraMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingestEnabled", constant = "true")
    @Mapping(target = "ingestNode", ignore = true)
    CameraEntity toEntity(CameraCreateDTO dto);

    CameraResponseDTO toDto(CameraEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingestNode", ignore = true)
    CameraEntity update(CameraUpdateDTO dto, @org.mapstruct.MappingTarget CameraEntity entity);
}
