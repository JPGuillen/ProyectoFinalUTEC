package com.julygt.ProyectoFinalUTEC.categorias;

public class CategoriaNotFoundException extends RuntimeException {

    public CategoriaNotFoundException(Long idCategoria) {
        super("Categoría con id " + idCategoria + " no encontrada");
    }

    public CategoriaNotFoundException(String nombre) {
        super("No se encontraron categorías que coincidan con: " + nombre);
    }
}
