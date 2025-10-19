package com.julygt.ProyectoFinalUTEC.auth;

public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    // Usuario ya existe
    public static class UserAlreadyExistsException extends AuthException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    // Credenciales inválidas
    public static class InvalidCredentialsException extends AuthException {
        public InvalidCredentialsException() {
            super("Credenciales inválidas");
        }
    }

    // Datos inválidos (ej: rol no enviado o nombreTienda faltante)
    public static class InvalidDataException extends AuthException {
        public InvalidDataException(String message) {
            super(message);
        }
    }
}
