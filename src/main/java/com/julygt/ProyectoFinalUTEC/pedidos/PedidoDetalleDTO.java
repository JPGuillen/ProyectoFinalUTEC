package com.julygt.ProyectoFinalUTEC.pedidos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDetalleDTO {
    private String producto;      // Nombre del producto
    private Integer cantidad;
    private Double precioUnitario;
    //private Double subtotal;
    private String vendedor;      // Nombre público del vendedor (sin ID)

    // Calcula el subtotal dinámicamente
    public Double getSubtotal() {
        if (precioUnitario == null || cantidad == null) return 0.0;
        return precioUnitario * cantidad;
    }

    public static PedidoDetalleDTO fromEntity(PedidoDetalle d) {
        return new PedidoDetalleDTO(
                d.getProducto() != null ? d.getProducto().getNombre() : null,
                d.getCantidad(),
                d.getPrecioUnitario(),
                //d.getSubtotal(),
                // 🔹 Se obtiene el vendedor desde el producto asociado
                d.getProducto() != null && d.getProducto().getVendedor() != null
                        ? d.getProducto().getVendedor().getNombrePublicoTienda()
                        : null
        );
    }
}


