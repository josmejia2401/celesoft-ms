package com.celesoft.users.mapper;

import com.celesoft.entities.security.UserEntity;
import com.celesoft.users.dto.UserDTO;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "createdAt", ignore = true)
    UserEntity toEntity(UserDTO dto);

    UserDTO toDto(UserEntity entity);

    @Mapping(target = "statusId", ignore = true)
    void updateEntityFromDto(UserDTO dto, @MappingTarget UserEntity entity);
}
