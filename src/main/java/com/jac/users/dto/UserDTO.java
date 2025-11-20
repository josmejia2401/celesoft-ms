package com.jac.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @NotNull(message = "El nombre no puede ser vacía")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @NotNull(message = "El apellido no puede ser vacía")
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    @NotNull(message = "La email no puede ser vacía")
    private String email;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @NotNull(message = "La número de teléfono no puede ser vacía")
    private String phoneNumber;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @NotNull(message = "La nombre de usuario no puede ser vacía")
    private String username;

    private Long statusId;
    private Long previousStatusId;
    private UserSecurityDTO security;

    @NotBlank(message = "La contraseña es obligatoria")
    @NotNull(message = "La contraseña no puede ser vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private Instant createdAt;
}
