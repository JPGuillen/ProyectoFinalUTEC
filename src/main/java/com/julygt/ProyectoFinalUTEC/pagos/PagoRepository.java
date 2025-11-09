package com.julygt.ProyectoFinalUTEC.pagos;

import com.julygt.ProyectoFinalUTEC.pedidos.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByPedido(Pedido pedido);
}
