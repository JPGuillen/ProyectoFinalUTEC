package com.julygt.ProyectoFinalUTEC.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface ProductoRepository extends JpaRepository<ProductoBD, Long> {

    // Buscar x nombre contenga una palabra (mayúsculas/minúsculas)
    @Query("Select p From ProductoBD p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<ProductoBD> buscarPorNombre(String nombre);

}

