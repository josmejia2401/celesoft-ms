package com.jac.utils.enums;

import java.util.Arrays;

public enum UserRolesEnum {

    USER("USER", "Usuario estándar del sistema"),
    ADMIN("ADMIN", "Administrador con acceso completo");

    private final String code;
    private final String description;

    UserRolesEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }

    public static UserRolesEnum fromCode(String code) {
        return Arrays.stream(values())
                .filter(r -> r.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }
}
