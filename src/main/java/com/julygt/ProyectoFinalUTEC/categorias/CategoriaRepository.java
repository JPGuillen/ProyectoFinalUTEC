package com.julygt.ProyectoFinalUTEC.categorias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository  extends  JpaRepository <Categoria, Long>{

    // Busca categoría cuyo nombre contenga el texto (ignorando mayúsculas/minúsculas)
    // Optional<Categoria> findFirstByNombreContainingIgnoreCase(String nombre);

     // Optimiza la class buscarPorNombreParcial
     List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    //return categoriaRepository.findByNombreContainingIgnoreCase(nombre);

}


