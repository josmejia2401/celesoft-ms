package com.jac.common.mapper;

import com.jac.common.dto.StatusDTO;
import com.jac.utils.enums.StatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusMapper INSTANCE = Mappers.getMapper(StatusMapper.class);

    @Mapping(target = "isActive", source = "active")
    StatusDTO toDto(StatusEnum status);
}
