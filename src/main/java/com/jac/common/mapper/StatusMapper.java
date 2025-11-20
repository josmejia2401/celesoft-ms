package com.celesoft.common.mapper;

import com.celesoft.common.dto.StatusDTO;
import com.celesoft.utils.enums.StatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusMapper INSTANCE = Mappers.getMapper(StatusMapper.class);
    StatusDTO toDto(StatusEnum status);
}
