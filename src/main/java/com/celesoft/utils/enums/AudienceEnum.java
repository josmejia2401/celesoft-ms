package com.celesoft.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum AudienceEnum {

    WEB("web", "Aplicaciones WEB"),
    APP("app", "Aplicaciones APP");

    private final String code;
    private final String description;

    public static AudienceEnum fromCode(String code) {
        for (AudienceEnum status : values()) {
            if (Objects.equals(status.getCode(), code)) {
                return status;
            }
        }
        return null;
    }
}

