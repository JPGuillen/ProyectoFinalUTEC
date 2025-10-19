package com.julygt.ProyectoFinalUTEC.pedidos;

public class PedidoException extends RuntimeException {
    public PedidoException(String message) { super(message); }

    public static class NotFoundException extends PedidoException {
        public NotFoundException(String message) { super("Pedido no encontrado: " ); }
    }
}

