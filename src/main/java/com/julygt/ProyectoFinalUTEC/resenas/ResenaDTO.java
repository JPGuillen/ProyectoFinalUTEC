package com.julygt.ProyectoFinalUTEC.resenas;

import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.Producto.ProductoBD;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaDTO {
    private Long id_resena;
    private Usuario cliente;
    private ProductoBD producto;
    private Integer calificacion;
    private String comentario;
}

