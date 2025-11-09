package com.julygt.ProyectoFinalUTEC.carrito;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoBD;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Data               // getters, setters, toString, equals, hashCode
@NoArgsConstructor  // constructor vacío
@AllArgsConstructor // constructor (todos los campos)
@Entity
@Table(name = "carrito_detalle")
public class CarritoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_detalle")
    private Long id;

   // Relación con carrito
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrito", nullable = false)
    private Carrito carrito;

    // Relación con producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoBD producto;

    // Cantidad de unidades de ese producto
    @Column(nullable = false)
    private Integer cantidad;
}
