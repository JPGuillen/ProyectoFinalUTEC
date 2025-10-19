package com.julygt.ProyectoFinalUTEC.carrito;

public class CarritoException extends RuntimeException {
    public CarritoException(String message) {
        super(message);
    }

    public static class NotFoundException extends CarritoException {
        public NotFoundException(String message) {
            super(message);
        }
    }
}
