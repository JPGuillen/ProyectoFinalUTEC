package com.julygt.ProyectoFinalUTEC.notificaciones;

import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    // Crear notificación para un usuario
    public void crearNotificacion(Long idUsuario, String mensaje) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Notificacion notificacion = Notificacion.builder()
                .usuario(usuario)
                .mensaje(mensaje)
                .leido(false)                       // marca por defecto - no leída
                .fechaCreacion(LocalDateTime.now()) // asignar fecha de creación
                .build();

        notificacionRepository.save(notificacion); // guardar en BD
    }

    // Listar notificaciones del usuario autenticado
    public List<Notificacion> listarPorUsuario(Usuario usuario) {
        return notificacionRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }

    // Marcar como leída
    public void marcarComoLeida(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada"));
        notificacion.setLeido(true);
        notificacionRepository.save(notificacion);
    }
}
