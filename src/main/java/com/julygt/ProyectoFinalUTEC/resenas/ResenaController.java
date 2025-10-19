package com.julygt.ProyectoFinalUTEC.resenas;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @GetMapping
    public List<Resena> listar() { return resenaService.listarTodas(); }

    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtener(@PathVariable Long id) {
        return resenaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Resena crear(@RequestBody Resena resena) {
        return resenaService.guardar(resena);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizar(@PathVariable Long id, @RequestBody Resena resena) {
        return resenaService.obtenerPorId(id)
                .map(r -> {
                    r.setCalificacion(resena.getCalificacion());
                    r.setComentario(resena.getComentario());
                    return ResponseEntity.ok(resenaService.guardar(r));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

