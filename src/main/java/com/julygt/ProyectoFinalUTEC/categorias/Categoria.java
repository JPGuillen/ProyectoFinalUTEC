package com.julygt.ProyectoFinalUTEC.categorias;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Data               // getters, setters, toString, equals, hashCode
@NoArgsConstructor  // constructor vacío
@AllArgsConstructor // constructor (todos los campos)
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Serial
    @Column(name = "id_categoria")
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

}
