package com.julygt.ProyectoFinalUTEC.carrito;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Data               // getters, setters, toString, equals, hashCode
@NoArgsConstructor  // constructor vacío
@AllArgsConstructor // constructor (todos los campos)
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long id;

    // Cada carrito pertenece ->  1 usuario (cliente)
    @ManyToOne(fetch = FetchType.LAZY)  // Mejora rendimiento (no carga todo el usuario completo al traer el carrito)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;


    // Relación con detalles del carrito
    // 1 carrito puede tener varios detalles (productos dentro)
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoDetalle> detalles;

    // Getters y Setters
   }
