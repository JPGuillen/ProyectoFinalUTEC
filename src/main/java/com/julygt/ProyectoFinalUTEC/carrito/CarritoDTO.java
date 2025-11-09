package com.julygt.ProyectoFinalUTEC.carrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// DTO para exponer solo lo necesario al frontend
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDTO {

    private Long id_carrito;
    private Long id_cliente;
    private String nombreCliente;  // opcional, para mostrar en el frontend
    List<DetalleDTO> detalles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleDTO {
        private Long id_detalle;
//        private ProductoBD producto; // NO es correcto por la exposicion de datoss
        private String nombreProducto; // útil para el frontend
        private Double precio;  // se obtiene del producto
        private Integer cantidad;
    }
}

