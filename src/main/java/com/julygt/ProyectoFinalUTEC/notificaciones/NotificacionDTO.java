package com.julygt.ProyectoFinalUTEC.notificaciones;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacionDTO {

    private Long id;
    private String mensaje;
    private boolean leido;
    private LocalDateTime fechaCreacion;
    private Long idUsuario;

    // Conversión de entidad a DTO
    public static NotificacionDTO fromEntity(Notificacion notificacion) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(notificacion.getId());
        dto.setMensaje(notificacion.getMensaje());
        dto.setLeido(notificacion.isLeido());
        dto.setFechaCreacion(notificacion.getFechaCreacion());
        dto.setIdUsuario(notificacion.getUsuario().getId());
        return dto;
    }
}
