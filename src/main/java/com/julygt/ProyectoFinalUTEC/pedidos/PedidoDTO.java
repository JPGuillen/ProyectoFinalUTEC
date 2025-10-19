package com.julygt.ProyectoFinalUTEC.pedidos;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoBD;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Long id_pedido;
    private Usuario cliente;
    private LocalDateTime fecha;
    private String estado;
    private List<DetalleDTO> detalles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleDTO {
        private Long id_detalle;
        private ProductoBD producto;
        private Integer cantidad;
    }
}
