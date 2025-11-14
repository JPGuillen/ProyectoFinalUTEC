package com.julygt.ProyectoFinalUTEC.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.julygt.ProyectoFinalUTEC.usuario.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AuthDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "Nombre de usuario o email es obligatorio")
        private String usernameOrEmail;  // Se modifico x el Front

        @NotBlank(message = "La contraseña es obligatoria")
        private String contrasena;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
        private String username;   // corresponde a 'nombre' en Usuario

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato válido")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        private String contrasena;

        // ✅ Permite que el JSON use la clave "rol" (coincide con lo que envía el frontend)
        @JsonProperty("rol")
        @NotNull(message = "El rol es obligatorio")
        private Role role;             // CLIENTE o VENDEDOR

        private String nombreTienda;   // obligatorio solo si role = VENDEDOR
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String email;
        private String role;
        private String nombreTienda;       // opcional, solo para vendedores
        private LocalDateTime fechaRegistro;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String error;
        private String message;
        private int status;
    }
}
