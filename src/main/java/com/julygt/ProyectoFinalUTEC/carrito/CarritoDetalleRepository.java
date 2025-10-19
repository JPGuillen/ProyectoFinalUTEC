package com.julygt.ProyectoFinalUTEC.carrito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Long> {
}
