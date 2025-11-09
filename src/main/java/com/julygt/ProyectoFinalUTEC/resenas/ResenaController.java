package com.julygt.ProyectoFinalUTEC.resenas;

import com.julygt.ProyectoFinalUTEC.usuario.Role;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.usuario.UsuarioRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

@RestController
@RequestMapping("api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar reseñas de un producto (visibilidad todos)
    @GetMapping("/producto/{id}")
    public ResponseEntity<List<ResenaDTO>> listarPorProducto(@PathVariable Long id) {
        List<ResenaDTO> response = resenaService.listarPorProducto(id);
        return ResponseEntity.ok(response);
    }

    // Crear una nueva reseña (Clientes logueados)
    @PostMapping
    public ResponseEntity<String> crearResena(@RequestBody ResenaDTO resenaDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getRole() != Role.CLIENTE) {
            return ResponseEntity.status(403).body("Solo clientes pueden crear reseñas");
        }

        resenaService.crearResena(resenaDTO, usuario);
        return ResponseEntity.ok("Reseña creada correctamente");
    }

    @GetMapping("/producto/{id}/promedio")
    public ResponseEntity<Double> obtenerPromedio(@PathVariable Long id) {
        Double promedio = resenaService.obtenerPromedioCalificacion(id);
        return ResponseEntity.ok(promedio);
    }

}