package com.celesoft.conductors.mapper;

import com.celesoft.conductors.dto.ApiKeyDto;
import com.celesoft.entities.notifications.ApiKeyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StatusMapper.class})
public interface ApiKeyMapper {

    ApiKeyDto toDto(ApiKeyEntity entity);

    ApiKeyEntity toEntity(ApiKeyDto dto);
}

