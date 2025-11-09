package com.julygt.ProyectoFinalUTEC.pedidos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
private Long id;
private Double total;
private String estado;
private LocalDateTime fechaCreacion;
private String direccionEnvio;
private List<PedidoDetalleDTO> detalles;

public static PedidoDTO fromEntity(Pedido pedido) {
    PedidoDTO dto = new PedidoDTO();
    dto.setId(pedido.getId());
    dto.setTotal(pedido.getTotal());
    dto.setEstado(pedido.getEstado());
    dto.setFechaCreacion(pedido.getFechaCreacion());
    dto.setDireccionEnvio(pedido.getDireccionEnvio());
    dto.setDetalles(
            pedido.getDetalles() != null
                    ? pedido.getDetalles().stream().map(PedidoDetalleDTO::fromEntity).toList()
                    : null
    );

    // 🔹 Convertimos los detalles a DTO y se calcula subtotal dinámicamente
   /* dto.setDetalles(
            pedido.getDetalles() != null
                    ? pedido.getDetalles().stream()
                    .map(PedidoDetalleDTO::fromEntity)
                    .toList()
                    : null
    );
*/
    return dto;
    }
}
