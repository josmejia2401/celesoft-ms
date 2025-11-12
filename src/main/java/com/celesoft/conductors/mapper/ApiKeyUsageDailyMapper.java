package com.celesoft.conductors.mapper;

import com.celesoft.conductors.dto.ApiKeyUsageDailyDto;
import com.celesoft.entities.notifications.ApiKeyUsageDailyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApiKeyUsageDailyMapper {

    ApiKeyUsageDailyDto toDto(ApiKeyUsageDailyEntity entity);

    ApiKeyUsageDailyEntity toEntity(ApiKeyUsageDailyDto dto);
}

