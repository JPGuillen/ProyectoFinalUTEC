package com.julygt.ProyectoFinalUTEC.resenas;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoBD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    // Busca todas las reseñas de un producto específico
    List<Resena> findByProducto(ProductoBD producto);

    // Calcula el promedio de calificaciones de un producto por su ID
    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.producto.id = :idProducto")
    Optional<Double> promedioCalificacionPorProducto(Long idProducto);
}

