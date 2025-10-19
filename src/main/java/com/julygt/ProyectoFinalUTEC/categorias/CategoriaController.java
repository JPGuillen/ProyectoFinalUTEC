package com.julygt.ProyectoFinalUTEC.categorias;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtener(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

 //   @PostMapping
 //   public Categoria crear(@RequestBody Categoria categoria) {
 //       return categoriaService.guardar(categoria);}

    @PostMapping
    public CategoriaDTO crear(@RequestBody CategoriaDTO dto) {
        Categoria categoria = new Categoria(); // crear entidad nueva
        categoria.setNombre(dto.getNombre()); // muestra nombre categoria no id
        Categoria guardada = categoriaService.guardar(categoria); // guardamos en DB
        return new CategoriaDTO(guardada.getId_categoria(), guardada.getNombre()); // devolve DTO
    }


    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.obtenerPorId(id)
                .map(c -> {
                    categoria.setId_categoria(id);
                    return ResponseEntity.ok(categoriaService.guardar(categoria));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Se agrego buscar x nombre
    @PutMapping("/{nombre}")
    public ResponseEntity<CategoriaDTO> actualizar(@PathVariable String nombre, @RequestBody CategoriaDTO dto) {
        return categoriaService.obtenerPorNombre(nombre)
                .map(c -> {
                    c.setNombre(dto.getNombre()); // actualizar con el nombre del DTO
                    Categoria actualizada = categoriaService.guardar(c);
                    return new CategoriaDTO(actualizada.getId_categoria(), actualizada.getNombre());
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // En esta entidad no es necesario el @PatchMapping

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (categoriaService.obtenerPorId(id).isPresent()) {
            categoriaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}

