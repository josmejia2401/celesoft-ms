package com.jac.cameras.mapper;


import com.jac.cameras.dto.EventRequestDTO;
import com.jac.cameras.dto.EventResponseDTO;
import com.jac.entities.camera.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    EventEntity toEntity(EventRequestDTO dto);

    EventResponseDTO toDto(EventEntity event);
}
