package com.celesoft.auth.mapper;

import com.celesoft.auth.dto.UserDTO;
import com.celesoft.entities.security.UserEntity;
import org.mapstruct.*;

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
