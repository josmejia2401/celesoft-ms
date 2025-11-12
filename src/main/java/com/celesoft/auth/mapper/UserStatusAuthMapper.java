package com.celesoft.auth.mapper;


import com.celesoft.auth.dto.UserStatusDTO;
import com.celesoft.entities.core.UserStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserStatusAuthMapper {
    UserStatusAuthMapper INSTANCE = Mappers.getMapper(UserStatusAuthMapper.class);

    UserStatusDTO toDto(UserStatusEntity entity);
    UserStatusEntity toEntity(UserStatusDTO dto);
}
