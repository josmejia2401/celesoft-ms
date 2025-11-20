package com.celesoft.auth.mapper;

import com.celesoft.auth.dto.PasswordResetDTO;
import com.celesoft.entities.security.PasswordResetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PasswordResetMapper {
    PasswordResetMapper INSTANCE = Mappers.getMapper(PasswordResetMapper.class);

    PasswordResetDTO toDto(PasswordResetEntity entity);
    PasswordResetEntity toEntity(PasswordResetDTO dto);
}
