package com.celesoft.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInDTO {

    @NotBlank(message = "El Usuario es requerido")
    @NotNull(message = "El Usuario no puede ser vacío")
    private String username;

    @NotBlank(message = "La Contraseña es requerido")
    @NotNull(message = "El Contraseña no puede ser vacío")
    private String password;

    private String audience;

    private String appName;
}
