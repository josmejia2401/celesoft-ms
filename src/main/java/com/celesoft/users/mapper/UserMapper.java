package com.celesoft.users.mapper;

import com.celesoft.entities.core.UserEntity;
import com.celesoft.users.dto.UserDTO;
import com.celesoft.users.dto.UserSecurityDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    @Mapping(target = "createdAt", ignore = true)
    public abstract UserEntity toEntity(UserDTO dto);

    @Mapping(target = "security", expression = "java(toSecurityDto(entity.getSecurity()))")
    public abstract UserDTO toDto(UserEntity entity);

    @AfterMapping
    protected void afterToEntity(UserDTO dto, @MappingTarget UserEntity entity) {
        if (dto.getSecurity() != null) {
            entity.setSecurity(toSecurityEntity(dto.getSecurity()));
        }
    }

    protected UserSecurityDTO toSecurityDto(String json) {
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, UserSecurityDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON a SecurityDTO", e);
        }
    }

    protected String toSecurityEntity(UserSecurityDTO dto) {
        if (dto == null) return null;
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir SecurityDTO a JSON", e);
        }
    }

    public abstract void updateEntityFromDto(UserDTO dto, @MappingTarget UserEntity entity);
}
