package com.jac.auth.mapper;

import com.jac.auth.dto.PasswordResetDTO;
import com.jac.entities.security.PasswordResetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PasswordResetMapper {
    PasswordResetMapper INSTANCE = Mappers.getMapper(PasswordResetMapper.class);

    PasswordResetDTO toDto(PasswordResetEntity entity);
    PasswordResetEntity toEntity(PasswordResetDTO dto);
}
