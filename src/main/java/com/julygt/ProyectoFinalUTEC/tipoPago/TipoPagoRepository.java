package com.julygt.ProyectoFinalUTEC.tipoPago;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPagoRepository extends JpaRepository<TipoPago, Long> {
}

