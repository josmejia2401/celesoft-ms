package com.celesoft.users.mapper;


import com.celesoft.entities.core.UserStatusEntity;
import com.celesoft.users.dto.UserStatusDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {
    UserStatusMapper INSTANCE = Mappers.getMapper(UserStatusMapper.class);

    UserStatusDTO toDto(UserStatusEntity entity);
    UserStatusEntity toEntity(UserStatusDTO dto);
}
