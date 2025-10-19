package com.julygt.ProyectoFinalUTEC.notificaciones;

import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {
    private Long id_notificacion;
    private Usuario usuario;
    private String mensaje;
    private LocalDateTime fecha;
    private boolean leida;
}

