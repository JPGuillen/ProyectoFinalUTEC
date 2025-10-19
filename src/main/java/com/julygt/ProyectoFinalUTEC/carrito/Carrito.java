package com.julygt.ProyectoFinalUTEC.carrito;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrito;

    // Relación con Usuario (cliente)
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario cliente;

    // Relación con detalles del carrito
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoDetalle> detalles;

    // Getters y Setters
    public Long getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(Long id_carrito) {
        this.id_carrito = id_carrito;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public List<CarritoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CarritoDetalle> detalles) {
        this.detalles = detalles;
    }
}
