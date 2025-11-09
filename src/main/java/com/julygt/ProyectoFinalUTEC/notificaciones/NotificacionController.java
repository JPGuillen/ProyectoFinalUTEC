package com.julygt.ProyectoFinalUTEC.notificaciones;

import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public List<NotificacionDTO> listar(Authentication auth) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // ✅ Convertimos a DTO antes de devolver
        return notificacionService.listarPorUsuario(usuario)
                .stream()
                .map(NotificacionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/leida")
    public ResponseEntity<Map<String, String>> marcarLeida(@PathVariable Long id) {
        notificacionService.marcarComoLeida(id);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Notificación leída");

        return ResponseEntity.ok(respuesta);
    }

}

