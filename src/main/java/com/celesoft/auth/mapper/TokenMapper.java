package com.celesoft.auth.mapper;

import com.celesoft.auth.dto.TokenDTO;
import com.celesoft.entities.core.TokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);

    TokenDTO toDto(TokenEntity entity);
    TokenEntity toEntity(TokenDTO dto);
}