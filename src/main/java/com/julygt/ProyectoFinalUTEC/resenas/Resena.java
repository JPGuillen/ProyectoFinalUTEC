package com.julygt.ProyectoFinalUTEC.resenas;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoBD;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_resena;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoBD producto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private Integer calificacion; // 1-5

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @CreationTimestamp
    private LocalDateTime fecha_creacion;

    // Getters y Setters
    public Long getId_resena() { return id_resena; }
    public void setId_resena(Long id_resena) { this.id_resena = id_resena; }

    public ProductoBD getProducto() { return producto; }
    public void setProducto(ProductoBD producto) { this.producto = producto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(LocalDateTime fecha_creacion) { this.fecha_creacion = fecha_creacion; }
}

