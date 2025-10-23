package com.julygt.ProyectoFinalUTEC.categorias;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoException;
import com.julygt.ProyectoFinalUTEC.usuario.Role;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Método auxiliar (usuario autenticado)
    private Usuario getUsuarioActual() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario usuarioActual)) {
            throw new ProductoException.NoAutorizadoException("Usuario no autenticado");
        }
        return usuarioActual;
    }

    // Listar categorías (globales, visibles para todos los roles)
    // Sera visible(Cliente / Vendedor) en el frontend
    @GetMapping
    public List<CategoriaDTO> listar() {
        List<Categoria> categorias = categoriaService.listarTodas();

        return categorias.stream()
                .map(c -> new CategoriaDTO(c.getId_categoria(), c.getNombre()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtener(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crea categoria Solo vendedor
    @PostMapping
    public CategoriaDTO crear(@RequestBody CategoriaDTO dto) {
        // Autentificacion
        Usuario vendedorActual = getUsuarioActual();

        if (vendedorActual.getRole() != Role.VENDEDOR) {
            throw new ProductoException.AccesoProhibidoException("Solo vendedores pueden crear categorías");
        }

        // Evita duplicados (nombre)
        categoriaService.obtenerPorNombre(dto.getNombre())
                .ifPresent(c -> {
                    throw new ProductoException.DatosInvalidosException("Ya existe una categoría con ese nombre");
                });

        Categoria categoria = new Categoria(); // crear entidad nueva
        categoria.setNombre(dto.getNombre());  // muestra nombre categoria no id
        Categoria guardada = categoriaService.guardar(categoria); // guardamos en DB
        return new CategoriaDTO(guardada.getId_categoria(), guardada.getNombre()); // devolve DTO
    }

    // No se permite modificar ni eliminar categorías (mantener consistencia global)
    // Por lo tanto, no se incluye @PutMapping ni @DeleteMapping

}
