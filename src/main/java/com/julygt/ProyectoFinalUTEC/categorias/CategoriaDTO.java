package com.julygt.ProyectoFinalUTEC.categorias;
import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;
    private String nombre;

    public static CategoriaDTO fromEntity(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
       dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        return dto;
    }
}

