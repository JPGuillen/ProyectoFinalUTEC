package com.julygt.ProyectoFinalUTEC.Producto;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoBD;
import com.julygt.ProyectoFinalUTEC.Producto.ProductoServiceBD;
import com.julygt.ProyectoFinalUTEC.categorias.CategoriaService;
import com.julygt.ProyectoFinalUTEC.usuario.Role;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productosBD") // Esto viene de la BAse de datos (Productos)

public class ProductoControllerBD {

    private final ProductoServiceBD productoService;
    private final CategoriaService categoriaService; // <-- servicio de categorías

    public ProductoControllerBD(ProductoServiceBD productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<ProductoBD> listar() {
        return productoService.listarTodos();
    }

    // Buscar productos por nombre parcial
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<ProductoBD>> buscarPorNombre(@PathVariable String nombre) {
        List<ProductoBD> resultados = productoService.buscarPorNombre(nombre);
        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 sin contenido
        }
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoBD> obtener(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
/* --Antes
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProductoBD producto) {
        // Obtener el usuario autenticado desde Spring Security
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario vendedorActual)) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "No autorizado",
                    "message", "Usuario no autenticado"
            ));
        }

        if (vendedorActual.getRole() != Role.vendedor) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "Prohibido",
                    "message", "Solo vendedores pueden crear productos"
            ));
        }

        producto.setVendedor(vendedorActual);
        ProductoBD guardado = productoService.guardar(producto);
        return ResponseEntity.ok(guardado);
    }
*/
// Ahora
@PostMapping
public ResponseEntity<?> crear(@RequestBody Map<String, Object> requestBody) {
    // Usuario autenticado
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!(principal instanceof Usuario vendedorActual)) {
        return ResponseEntity.status(401).body(Map.of(
                "error", "No autorizado",
                "message", "Usuario no autenticado"
        ));
    }

    if (vendedorActual.getRole() != Role.vendedor) {
        return ResponseEntity.status(403).body(Map.of(
                "error", "Prohibido",
                "message", "Solo vendedores pueden crear productos"
        ));
    }

    // Crear producto
    ProductoBD producto = new ProductoBD();
    producto.setNombre((String) requestBody.get("nombre"));
    producto.setPrecio(Double.valueOf(requestBody.get("precio").toString()));
    producto.setStock(Integer.valueOf(requestBody.get("stock").toString()));
    producto.setVendedor(vendedorActual);

    // Asignar categoría
    if (!requestBody.containsKey("id_categoria")) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Categoría requerida",
                "message", "Debes indicar id_categoria"
        ));
    }
    Long categoriaId = Long.valueOf(requestBody.get("id_categoria").toString());
    var categoriaOpt = categoriaService.obtenerPorId(categoriaId);
    if (categoriaOpt.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Categoría no encontrada",
                "message", "No existe categoría con id " + categoriaId
        ));
    }
    producto.setCategoria(categoriaOpt.get());

    // Guardar producto
    ProductoBD guardado = productoService.guardar(producto);
    return ResponseEntity.ok(guardado);
}





    @PutMapping("/{id}")
    public ResponseEntity<ProductoBD> actualizar(@PathVariable Long id, @RequestBody ProductoBD producto) {
        return productoService.obtenerPorId(id)
                .map(p -> {
                    producto.setId_producto(id);
                    return ResponseEntity.ok(productoService.guardar(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualiza una parte de las columnas
    @PatchMapping("/{id}")
    public ResponseEntity<ProductoBD> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return productoService.obtenerPorId(id)
                .map(producto -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "nombre":
                                producto.setNombre((String)value);
                                break;
                            case "precio":
                                producto.setPrecio(Double.valueOf(value.toString()));
                                break;
                            case "stock":
                                producto.setStock(Integer.valueOf(value.toString()));
                                break;
                        }
                    });
                    return ResponseEntity.ok(productoService.guardar(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (productoService.obtenerPorId(id).isPresent()) {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
