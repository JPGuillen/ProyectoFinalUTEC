package com.julygt.ProyectoFinalUTEC.categorias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface CategoriaRepository  extends  JpaRepository <Categoria, Long>{

    // Busca la primera categoría cuyo nombre contenga el texto (ignorando mayúsculas/minúsculas)
     Optional<Categoria> findFirstByNombreContainingIgnoreCase(String nombre);
}


