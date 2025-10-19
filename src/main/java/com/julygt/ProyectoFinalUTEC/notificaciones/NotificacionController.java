package com.julygt.ProyectoFinalUTEC.notificaciones;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<Notificacion> listar() { return notificacionService.listarTodas(); }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtener(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Notificacion crear(@RequestBody Notificacion notificacion) {
        return notificacionService.guardar(notificacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizar(@PathVariable Long id, @RequestBody Notificacion notificacion) {
        return notificacionService.obtenerPorId(id)
                .map(n -> {
                    n.setMensaje(notificacion.getMensaje());
                    n.setLeido(notificacion.getLeido());
                    return ResponseEntity.ok(notificacionService.guardar(n));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

