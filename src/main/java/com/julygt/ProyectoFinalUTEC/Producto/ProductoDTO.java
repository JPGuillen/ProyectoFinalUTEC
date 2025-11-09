package com.julygt.ProyectoFinalUTEC.Producto;
import com.julygt.ProyectoFinalUTEC.categorias.Categoria;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
    private String imagenUrl;
    private String nombreCategoria;
    private String nombreVendedor; // nombre público de la tienda (si existe)

    // ✅ Para cliente
    public static ProductoDTO fromEntityCliente(ProductoBD p) {
        if (p == null) return null;

        ProductoDTO dto = new ProductoDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setImagenUrl(p.getImagenUrl());

        // Evita NullPointer si no tiene categoría o vendedor
        dto.setNombreCategoria(p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría");
        dto.setNombreVendedor(p.getVendedor() != null
                ? (p.getVendedor().getNombrePublicoTienda() != null
                ? p.getVendedor().getNombrePublicoTienda()
                : p.getVendedor().getUsername())
                : "Sin vendedor");

        return dto;
    }

    // ✅ Para vendedor
    public static ProductoDTO fromEntityVendedor(ProductoBD p) {
        if (p == null) return null;

        ProductoDTO dto = new ProductoDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setImagenUrl(p.getImagenUrl());
        dto.setNombreCategoria(p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría");

        // El vendedor puede ser null (producto recién creado, error de referencia, etc.)
        Usuario vendedor = p.getVendedor();
        if (vendedor != null) {
            dto.setNombreVendedor(vendedor.getNombrePublicoTienda() != null
                    ? vendedor.getNombrePublicoTienda()
                    : vendedor.getUsername());
        } else {
            dto.setNombreVendedor("Sin vendedor");
        }

        return dto;
    }
}


    /*
    // static: no necesitas crear un objeto ProductoDTO antes de llamar al metodo.
    // Puedes llamarlo directamente con el nombre de la clase
    // fromEntity: crea un nuevo DTO a partir de la entidad, no depende de una instancia previa.
    fromEntity, es mas limpia en el service, no se hace
            return new ProductoDTO(
            p.getId(),
            p.getNombre(),
            p.getPrecio(),
            p.getStock(),
            p.getCategoria() != null ? p.getCategoria().getNombre() : null,
            p.getVendedor() != null ? p.getVendedor().getNombrePublicoTienda() : null
        );
    */
