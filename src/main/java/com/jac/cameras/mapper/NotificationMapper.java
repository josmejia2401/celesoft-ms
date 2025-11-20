package com.jac.cameras.mapper;

import com.jac.cameras.dto.NotificationCreateDTO;
import com.jac.cameras.dto.NotificationResponseDTO;
import com.jac.entities.camera.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    NotificationEntity toEntity(NotificationCreateDTO dto);

    NotificationResponseDTO toDto(NotificationEntity entity);
}
