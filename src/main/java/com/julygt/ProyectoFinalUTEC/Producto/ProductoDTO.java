
package com.julygt.ProyectoFinalUTEC.Producto;
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
    private String categoria;
    private String vendedor;
    private String imagenUrl;


    public ProductoDTO(Long id, String nombre, Double precio, Integer stock, String categoria, String vendedor) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.vendedor = vendedor;
    }

}