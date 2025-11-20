package com.jac.utils.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum StatusEnum {

    // Notifications
    QUEUED(1L, "notification", "queued", "En cola", "Notificación en cola, pendiente de envío", true),
    SENDING(2L, "notification", "sending", "Enviando", "En proceso de envío", true),
    SENT(3L, "notification", "sent", "Enviado", "Enviado al proveedor", true),
    FAILED(4L, "notification", "failed", "Fallido", "Entrega fallida", true),

    // API keys
    APIKEY_ACTIVE(5L, "api_key", "active", "Activo", "Clave API habilitada", true),
    APIKEY_DISABLED(6L, "api_key", "disabled", "Deshabilitado", "Clave temporalmente deshabilitada", true),
    APIKEY_REVOKED(7L, "api_key", "revoked", "Revocado", "Clave revocada permanentemente", false),

    // System
    SYSTEM_HEALTHY(8L, "system", "healthy", "Saludable", "Sistema operando normalmente", true),
    SYSTEM_DEGRADED(9L, "system", "degraded", "Degradado", "Rendimiento degradado / parcial", true),
    SYSTEM_MAINTENANCE(10L, "system", "maintenance", "Mantenimiento", "Mantenimiento programado", true),
    SYSTEM_OFFLINE(11L, "system", "offline", "Offline", "Sistema fuera de servicio", false);

    private final Long id;
    private final String category;
    private final String code;
    private final String name;
    private final String description;
    private final boolean isActive;

    StatusEnum(Long id, String category, String code, String name, String description, boolean isActive) {
        this.id = id;
        this.category = category;
        this.code = code;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    public Long getId() { return id; }
    public String getCategory() { return category; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return isActive; }

    // Lookup maps
    private static final Map<Long, StatusEnum> BY_ID =
            Arrays.stream(values()).collect(Collectors.toMap(StatusEnum::getId, s -> s));

    private static final Map<String, StatusEnum> BY_CATEGORY_CODE =
            Arrays.stream(values()).collect(Collectors.toMap(
                    s -> s.getCategory() + "|" + s.getCode(),
                    s -> s
            ));

    public static Optional<StatusEnum> fromId(Long id) {
        return Optional.ofNullable(BY_ID.get(id));
    }

    public static Optional<StatusEnum> fromCategoryAndCode(String category, String code) {
        if (category == null || code == null) return Optional.empty();
        return Optional.ofNullable(BY_CATEGORY_CODE.get(category + "|" + code));
    }
}
