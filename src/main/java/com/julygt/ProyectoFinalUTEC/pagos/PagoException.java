package com.julygt.ProyectoFinalUTEC.pagos;

public class PagoException {

    // Pedido no encontrado
    public static class PedidoNoEncontradoException extends RuntimeException {
        public PedidoNoEncontradoException(Long idPedido) {
            super("No se encontró el pedido con ID: " + idPedido);
        }
    }

    // Metodo de pago inválido
    public static class MetodoPagoInvalidoException extends RuntimeException {
        public MetodoPagoInvalidoException(String metodo) {
            super("Método de pago no válido: " + metodo + ". Use: YAPE, TARJETA, DEPOSITO o CONTRAENTREGA.");
        }
    }

    //  Otros errores de pago, (Dupliciad de pago)
    //  validaciones Externas (pasarela de Pago)
    public static class PagoFallidoException extends RuntimeException {
        public PagoFallidoException(String mensaje) {
            super("Error al procesar el pago: " + mensaje);  // opcional
        }
    }

}
