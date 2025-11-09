package com.julygt.ProyectoFinalUTEC.pedidos;

/**
 * Excepciones personalizadas para manejar errores relacionados con pedidos.
 * Estas clases se lanzan desde el Service y son capturadas por el GlobalExceptionHandler.
 */
public class PedidoException extends RuntimeException {

    public PedidoException(String message) {
        super(message);   // constructor
    }

    // Cuando un recurso (pedido, carrito, usuario, etc.) no se encuentra
    public static class NotFoundException extends PedidoException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    // Cuando ocurre un error de negocio (por ejemplo: carrito vacío)
    public static class BusinessException extends PedidoException {
        public BusinessException(String message) {
            super(message);
        }
    }
}
