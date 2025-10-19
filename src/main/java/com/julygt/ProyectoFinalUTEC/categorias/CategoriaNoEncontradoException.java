package com.julygt.ProyectoFinalUTEC.categorias;

public class CategoriaNoEncontradoException extends RuntimeException {
    public CategoriaNoEncontradoException(Long id_categoria) {
        super("Categoria con id " + id_categoria + " no encontrado");
    }
}

