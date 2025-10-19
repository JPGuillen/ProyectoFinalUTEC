
package com.julygt.ProyectoFinalUTEC.Producto;

import lombok.Setter;

public class ProductoDTO {
    @Setter
    private String nombre;
    private Double precioBase;

    public ProductoDTO(){}

    public  ProductoDTO( String nombre, Double precioBase){
        this.nombre =nombre;
        this.precioBase =precioBase;

    }

    public Double getprecioBase() {
        return precioBase;
    }

    public void setprecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

}