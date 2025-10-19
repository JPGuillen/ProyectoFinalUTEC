package com.julygt.ProyectoFinalUTEC.categorias;

import jakarta.persistence.*;
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Serial
    private Long id_categoria;

    private String nombre;

    public Long getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(Long id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
