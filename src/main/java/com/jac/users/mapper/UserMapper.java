package com.jac.users.mapper;

import com.jac.entities.security.UserEntity;
import com.jac.users.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

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
