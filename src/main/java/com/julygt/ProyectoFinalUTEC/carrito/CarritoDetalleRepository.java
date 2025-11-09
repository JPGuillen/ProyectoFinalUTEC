package com.julygt.ProyectoFinalUTEC.carrito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Long> {

    // Buscar un detalle específico del carrito
    Optional<CarritoDetalle> findByCarritoIdAndProductoId(Long carritoId, Long productoId);

}
