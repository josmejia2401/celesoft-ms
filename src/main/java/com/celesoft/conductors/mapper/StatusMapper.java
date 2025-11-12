package com.celesoft.conductors.mapper;


import com.celesoft.conductors.dto.StatusDto;
import com.celesoft.entities.notifications.StatusEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusDto toDto(StatusEntity entity);
    StatusEntity toEntity(StatusDto dto);
}

