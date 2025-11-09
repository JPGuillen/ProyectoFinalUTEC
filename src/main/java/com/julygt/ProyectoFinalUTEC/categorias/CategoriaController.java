package com.julygt.ProyectoFinalUTEC.categorias;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Listar todas las categorías
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        List<CategoriaDTO> categorias = categoriaService.listarTodas().stream()
                .map(CategoriaDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(categorias);
    }

    // Buscar categorías por nombre parcial
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<CategoriaDTO> categorias = categoriaService.buscarPorNombreParcial(nombre).stream()
                .map(CategoriaDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(categorias);
    }

    // ResponseEntity, sirve para un menejo de errores (HTTP) x ejemplo 200 OK, 404, etc.
}

