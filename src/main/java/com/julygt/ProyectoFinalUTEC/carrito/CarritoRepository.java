package com.julygt.ProyectoFinalUTEC.carrito;

import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Buscar carrito x cliente
    Optional<Carrito> findByUsuario(Usuario cliente);
}
