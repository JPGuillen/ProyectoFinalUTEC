package com.julygt.ProyectoFinalUTEC.resenas;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ResenaDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;

    //    @NotNull(message = "El ID del usuario es obligatorio")
    //    private Long idUsuario;

    @Min(1)
    @Max(5)
    private int calificacion;

    private String comentario;

    // solo para salida, no obligatorio en entrada
    private String nombreUsuario;

    // Metodo para crear DTO filtrado a partir de la entidad
    public static ResenaDTO fromEntity(Resena resena) {
        ResenaDTO dto = new ResenaDTO();
        dto.setIdProducto(resena.getProducto().getId());
        dto.setCalificacion(resena.getCalificacion());
        dto.setComentario(resena.getComentario());
        dto.setNombreUsuario(resena.getUsuario().getUsername()); //
        return dto;
    }
}
