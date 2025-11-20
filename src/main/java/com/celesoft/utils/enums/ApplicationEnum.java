package com.celesoft.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum ApplicationEnum {

    ACTIVE("jac", "Aplicación JAC");

    private final String code;
    private final String description;

    public static ApplicationEnum fromCode(String code) {
        for (ApplicationEnum status : values()) {
            if (Objects.equals(status.getCode(), code)) {
                return status;
            }
        }
        return null;
    }
}

