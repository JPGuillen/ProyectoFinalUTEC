package com.julygt.ProyectoFinalUTEC.notificaciones;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<Notificacion> listarTodas() { return notificacionRepository.findAll(); }

    public Optional<Notificacion> obtenerPorId(Long id) { return notificacionRepository.findById(id); }

    public Notificacion guardar(Notificacion notificacion) { return notificacionRepository.save(notificacion); }

    public void eliminar(Long id) { notificacionRepository.deleteById(id); }
}

