package com.julygt.ProyectoFinalUTEC.categorias;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Listar todas las categorías (disponible para todos)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    // Obtener categoría por ID
    public Categoria obtenerPorIdOExcepcion(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException(id));
    }

    // Buscar categorías por nombre parcial (ignora mayúsculas/minúsculas)
    public List<Categoria> buscarPorNombreParcial(String nombre) {
        List<Categoria> categorias = categoriaRepository.findByNombreContainingIgnoreCase(nombre);
        if (categorias.isEmpty()) {
            throw new CategoriaNotFoundException(nombre);
        }
        return categorias;
    }
}
