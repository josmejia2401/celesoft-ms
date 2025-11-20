package com.jac.auth.mapper;

import com.jac.auth.dto.TokenDTO;
import com.jac.entities.security.TokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);

    TokenDTO toDto(TokenEntity entity);
    TokenEntity toEntity(TokenDTO dto);
}