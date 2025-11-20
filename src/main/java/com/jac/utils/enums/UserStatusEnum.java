package com.jac.utils.enums;

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

    private final Long id;
    private final String description;

    public static UserStatusEnum fromId(Long id) {
        for (UserStatusEnum status : values()) {
            if (Objects.equals(status.getId(), id)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de estado de usuario inválido: " + id);
    }
}

