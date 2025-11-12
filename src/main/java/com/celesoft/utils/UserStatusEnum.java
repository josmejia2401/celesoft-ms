package com.celesoft.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum UserStatusEnum {

    ACTIVE(1L, "Activo"),
    INACTIVE(2L, "Inactivo"),
    LOCKED(3L, "Bloqueado"),
    DELETED(4L, "Eliminado"),
    PENDING(5L, "Pendiente de activación");

    private final Long code;
    private final String description;

    public static UserStatusEnum fromCode(Long code) {
        for (UserStatusEnum status : values()) {
            if (Objects.equals(status.getCode(), code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de estado de usuario inválido: " + code);
    }
}

