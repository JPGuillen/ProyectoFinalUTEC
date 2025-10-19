package com.julygt.ProyectoFinalUTEC.Producto;

public class ProductoException {

    public static class NoAutorizadoException extends RuntimeException {
        public NoAutorizadoException(String message) {
            super(message);
        }
    }

    public static class AccesoProhibidoException extends RuntimeException {
        public AccesoProhibidoException(String message) {
            super(message);
        }
    }

    public static class NoEncontradoExceptionCate extends RuntimeException {
        public NoEncontradoExceptionCate(Long id) {
            super("categoria con id " + id + " no encontrado");
        }
    }

    public static class NoEncontradoException extends RuntimeException {
        public NoEncontradoException(Long id) {
            super("Producto con id " + id + " no encontrado");
        }
    }
    public static class NoEncontradoExceptionNombre extends RuntimeException {
        public NoEncontradoExceptionNombre(String nombre) {
            super("No se encontraron productos con el nombre:" + nombre);
        }
    }

    public static class DatosInvalidosException extends RuntimeException {
        public DatosInvalidosException(String message) {
            super(message);
        }
    }
}