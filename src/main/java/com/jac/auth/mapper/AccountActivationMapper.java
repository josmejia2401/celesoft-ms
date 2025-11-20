package com.celesoft.auth.mapper;


import com.celesoft.auth.dto.AccountActivationDTO;
import com.celesoft.entities.security.AccountActivationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountActivationMapper {
    AccountActivationMapper INSTANCE = Mappers.getMapper(AccountActivationMapper.class);

    AccountActivationDTO toDto(AccountActivationEntity entity);
    AccountActivationEntity toEntity(AccountActivationDTO dto);
}
