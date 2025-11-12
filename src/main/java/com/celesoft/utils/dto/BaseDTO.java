package com.celesoft.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO<T> {
    private String message;
    private OffsetDateTime datetime;
    private T data;
    private BaseMetaDTO meta;

    public static <T> BaseDTO<T> ok(T data) {
        return BaseDTO.<T>builder()
                .message("Operación exitosa")
                .datetime(OffsetDateTime.now())
                .data(data)
                .build();
    }

    public static <T> BaseDTO<T> ok(T data, String message) {
        return BaseDTO.<T>builder()
                .message(message)
                .datetime(OffsetDateTime.now())
                .data(data)
                .build();
    }

    public static <T> BaseDTO<T> ok(T data, BaseMetaDTO baseMetaDTO) {
        return BaseDTO.<T>builder()
                .message("Operación exitosa")
                .datetime(OffsetDateTime.now())
                .data(data)
                .meta(baseMetaDTO)
                .build();
    }

    public static <T> BaseDTO<T> ok(T data, BaseMetaDTO baseMetaDTO, String message) {
        return BaseDTO.<T>builder()
                .message(message)
                .datetime(OffsetDateTime.now())
                .data(data)
                .meta(baseMetaDTO)
                .build();
    }

    public static <T> BaseDTO<T> error(String message) {
        return BaseDTO.<T>builder()
                .message(message)
                .datetime(OffsetDateTime.now())
                .build();
    }
}
