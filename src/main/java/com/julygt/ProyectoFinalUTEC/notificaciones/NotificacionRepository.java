package com.julygt.ProyectoFinalUTEC.notificaciones;

import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);

}