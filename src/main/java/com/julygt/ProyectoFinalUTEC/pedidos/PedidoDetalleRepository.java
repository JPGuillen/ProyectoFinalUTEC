package com.julygt.ProyectoFinalUTEC.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {

    // Consulta personalizada: pedidos por vendedor
    @Query("SELECT DISTINCT pd.pedido FROM PedidoDetalle pd WHERE pd.producto.vendedor.id = :idVendedor")
    List<Pedido> findDistinctPedidosByVendedor(Long idVendedor);
}
