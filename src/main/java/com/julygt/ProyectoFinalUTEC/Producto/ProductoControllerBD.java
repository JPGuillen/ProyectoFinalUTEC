package com.julygt.ProyectoFinalUTEC.Producto;

import com.julygt.ProyectoFinalUTEC.categorias.CategoriaService;
import com.julygt.ProyectoFinalUTEC.usuario.Role;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productosBD") // Esto viene de la BD(Productos)

public class ProductoControllerBD {

    private final ProductoServiceBD productoService;
    private final CategoriaService categoriaService; //

    public ProductoControllerBD(ProductoServiceBD productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    // @GetMapping
    //  public List<ProductoBD> listar() {
    //      return productoService.listarTodos();}

    @GetMapping
    public List<ProductoDTO> listar() {
        return productoService.listarTodos()
                .stream()
                .map(p -> new ProductoDTO(
                        p.getId_producto(),
                        p.getNombre(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getCategoria() != null ? p.getCategoria().getNombre() : null,
                        p.getVendedor() != null ? p.getVendedor().getNombrePublicoTienda() : null
                ))
                .toList();
    }

    // Buscar productos por nombre parcial
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<ProductoDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<ProductoDTO> resultados = productoService.buscarPorNombre(nombre).stream()
                .map(p -> new ProductoDTO(
                        p.getId_producto(),
                        p.getNombre(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getCategoria() != null ? p.getCategoria().getNombre() : null,
                        p.getVendedor() != null ? p.getVendedor().getNombrePublicoTienda() : null,
                        p.getImagen_url()
                ))
                .toList();
        if (resultados.isEmpty()) {
            // return ResponseEntity.noContent().build(); // 204 sin contenido
            throw new ProductoException.NoEncontradoExceptionNombre(nombre);
        }
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtener(@PathVariable Long id) {
        var producto = productoService.obtenerPorId(id)
                .orElseThrow(() -> new ProductoException.NoEncontradoException(id));

        ProductoDTO dto = new ProductoDTO(
                producto.getId_producto(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null,
                producto.getVendedor() != null ? producto.getVendedor().getNombrePublicoTienda() : null,
                producto.getImagen_url()
        );

        return ResponseEntity.ok(dto);
    }





    // crear producto solo vendedor
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> requestBody) {
        // Usuario autenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario vendedorActual)) {
            throw new ProductoException.NoAutorizadoException("Usuario no autenticado");
        }

        if (vendedorActual.getRole() != Role.vendedor) {
            throw new ProductoException.AccesoProhibidoException("Solo vendedores pueden crear productos");
        }

        // Crear producto
        ProductoBD producto = new ProductoBD();
        producto.setNombre((String) requestBody.get("nombre"));
        producto.setPrecio(Double.valueOf(requestBody.get("precio").toString()));
        producto.setStock(Integer.valueOf(requestBody.get("stock").toString()));
        producto.setVendedor(vendedorActual);

        // Asignar categoría
        if (!requestBody.containsKey("id_categoria")) {
            throw new ProductoException.DatosInvalidosException("Debes indicar id_categoria");
        }

        Long categoriaId = Long.valueOf(requestBody.get("id_categoria").toString());
        var categoriaOpt = categoriaService.obtenerPorId(categoriaId);
        if (categoriaOpt.isEmpty()) {
            throw new ProductoException.NoEncontradoExceptionCate(categoriaId);
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

    // Actualiza parcial (Vendedor)
    @PatchMapping("/{id}")
    public ResponseEntity<Object> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        // Obtener usuario autenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario vendedorActual)) {
            throw new ProductoException.NoAutorizadoException("Usuario no autenticado");
        }

        if (vendedorActual.getRole() != Role.vendedor) {
            throw new ProductoException.AccesoProhibidoException("Solo vendedores pueden actualizar productos");
        }
        var productoOpt = productoService.obtenerPorId(id);

        if (productoOpt.isEmpty()) {
            throw new ProductoException.NoEncontradoException(id);
        }

        ProductoBD producto = productoOpt.get();

        // Verificar que pertenece al vendedor actual
        if (!producto.getVendedor().getId().equals(vendedorActual.getId())) {
            throw new ProductoException.AccesoProhibidoException("No puedes modificar productos de otro vendedor");
        }

        // Aplicar cambios parciales
        updates.forEach((key, value) -> {
            switch (key) {
                case "nombre" -> producto.setNombre((String) value);
                case "precio" -> producto.setPrecio(Double.valueOf(value.toString()));
                case "stock" -> producto.setStock(Integer.valueOf(value.toString()));
            }
        });

        ProductoBD actualizado = productoService.guardar(producto);
        return ResponseEntity.ok(actualizado);
    }

    // Elimna producto solo vendedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable Long id) {
        // Usuario autenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario vendedorActual)) {
            throw new ProductoException.NoAutorizadoException("Usuario no autenticado");
        }

        if (vendedorActual.getRole() != Role.vendedor) {
            throw new ProductoException.AccesoProhibidoException("Solo vendedores pueden eliminar productos");
        }

        var productoOpt = productoService.obtenerPorId(id);

        if (productoOpt.isEmpty()) {
            throw new ProductoException.NoEncontradoException(id);
        }

        ProductoBD producto = productoOpt.get();

        // Verificar producto pertenece al vendedor actual
        if (!producto.getVendedor().getId().equals(vendedorActual.getId())) {
            throw new ProductoException.AccesoProhibidoException("No puedes eliminar productos de otro vendedor");
        }

        productoService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
