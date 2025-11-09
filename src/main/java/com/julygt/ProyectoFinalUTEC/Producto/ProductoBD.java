package com.julygt.ProyectoFinalUTEC.Producto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.julygt.ProyectoFinalUTEC.categorias.Categoria;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoBD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vendedor", nullable = false)
    @JsonIgnoreProperties({"productos", "password"}) // evita ciclos y expone solo lo necesario
    @JsonIgnore
    private Usuario vendedor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    @JsonIgnoreProperties({"productos"}) // evita ciclos
    private Categoria categoria;

    private String nombre;
    private Double precio;
    private Integer stock;

    @Column(name = "imagen_url")
    private String imagenUrl;
}