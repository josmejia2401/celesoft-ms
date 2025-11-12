package com.celesoft.conductors.mapper;

import com.celesoft.conductors.dto.NotificationDto;
import com.celesoft.entities.notifications.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StatusMapper.class})
public interface NotificationMapper {

    NotificationDto toDto(NotificationEntity entity);

    // mapping DTO -> Entity: set apiKey and status in service layer (or implement resolvers)
    NotificationEntity toEntity(NotificationDto dto);
}

