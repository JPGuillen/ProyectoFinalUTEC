package com.julygt.ProyectoFinalUTEC.carrito;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoBD;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
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
    private Usuario cliente;
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

