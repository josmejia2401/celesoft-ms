package com.jac.auth.mapper;

import com.jac.auth.dto.UserDTO;
import com.jac.entities.security.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserAuthMapper {

    @Mapping(target = "createdAt", ignore = true)
    UserEntity toEntity(UserDTO dto);

    UserDTO toDto(UserEntity entity);

    void updateEntityFromDto(UserDTO dto, @MappingTarget UserEntity entity);
}
