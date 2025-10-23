package com.julygt.ProyectoFinalUTEC.Producto;

import com.julygt.ProyectoFinalUTEC.categorias.CategoriaService;
import com.julygt.ProyectoFinalUTEC.usuario.Role;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

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

    // Método auxiliar (usuario autenticado)
    private Usuario getUsuarioActual() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario usuarioActual)) {
            throw new ProductoException.NoAutorizadoException("Usuario no autenticado");
        }
        return usuarioActual;
    }

    // Busca (Usuario Logueado)
    @GetMapping
    public List<ProductoDTO> listar(Authentication authentication) {
        Usuario usuarioActual = getUsuarioActual();

        List<ProductoBD> productos;

        if (usuarioActual.getRole() == Role.VENDEDOR) {
            // Filtra solo los productos del vendedor logueado
            productos = productoService.listarPorVendedor(usuarioActual.getId());
        } else {
            // Si es usuario (cliente), lista todos los productos visibles
            productos = productoService.listarTodos();
        }

        return productos.stream()
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
    }

    // Buscar productos por nombre parcial ok
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<ProductoDTO>> buscarPorNombre(
            @PathVariable String nombre,
            Authentication authentication) {

        // --- Verificar usuario autenticado ---
        Usuario usuarioActual = getUsuarioActual();

        List<ProductoBD> resultados;

        // --- Lógica según el rol ---
        if (usuarioActual.getRole() == Role.VENDEDOR) {
            // 🔹 El vendedor solo busca entre sus propios productos
            resultados = productoService.buscarPorNombreYVendedor(nombre, usuarioActual.getId());
        } else {
            // 🔹 El cliente (u otro rol) puede buscar entre todos los productos
            resultados = productoService.buscarPorNombre(nombre);
        }

        // --- Validar si no hay resultados ---
        if (resultados.isEmpty()) {
            throw new ProductoException.NoEncontradoExceptionNombre(nombre);
        }

        // --- Convertir a DTO ---
        List<ProductoDTO> productosDTO = resultados.stream()
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

        return ResponseEntity.ok(productosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtener(
            @PathVariable Long id,
            Authentication authentication) {

        //  Verificar usuario autenticado
        Usuario usuarioActual = getUsuarioActual();

        //  Buscar producto
        var producto = productoService.obtenerPorId(id)
                .orElseThrow(() -> new ProductoException.NoEncontradoException(id));

        //  Validar acceso según el rol
        if (usuarioActual.getRole() == Role.VENDEDOR &&
                !producto.getVendedor().getId().equals(usuarioActual.getId())) {

            throw new ProductoException.NoAutorizadoException(
                    "No tienes permiso para ver este producto (pertenece a otro vendedor)"
            );
        }

        // --- Convertir a DTO ---
        ProductoDTO dto = new ProductoDTO(
                producto.getId_producto(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null,
                producto.getVendedor() != null ? producto.getVendedor().getNombrePublicoTienda() : null,
                producto.getImagen_url()
        );

        // --- Retornar respuesta ---
        return ResponseEntity.ok(dto);
    }

    // crear producto solo vendedor
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> requestBody) {
        // Usuario autenticado
        Usuario vendedorActual = getUsuarioActual();

        if (vendedorActual.getRole() != Role.VENDEDOR) {
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
    public ResponseEntity<Object> actualizar(
            @PathVariable Long id,
            @RequestBody ProductoBD producto) {

        // autenticacion
        Usuario usuarioActual = getUsuarioActual();

        //   Verificar  VENDEDOR
        if (usuarioActual.getRole() != Role.VENDEDOR) {
            throw new ProductoException.AccesoProhibidoException("Solo vendedores pueden actualizar productos");
        }

        // Buscar el producto
        var productoOpt = productoService.obtenerPorId(id);
        if (productoOpt.isEmpty()) {
            throw new ProductoException.NoEncontradoException(id);
        }

        ProductoBD existente = productoOpt.get();

        // --- Verificar pertenezca al vendedor logueado
        if (!existente.getVendedor().getId().equals(usuarioActual.getId())) {
            throw new ProductoException.AccesoProhibidoException("No puedes modificar productos de otro vendedor");
        }

        // (Reemplaza los valores)
        producto.setId_producto(id);
        producto.setVendedor(existente.getVendedor()); // mantiene el vendedor original

        ProductoBD actualizado = productoService.guardar(producto);

        return ResponseEntity.ok(actualizado);
    }

    // Actualiza parcial (Vendedor)
    @PatchMapping("/{id}")
    public ResponseEntity<Object> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        // Obtener usuario autenticado
        Usuario vendedorActual = getUsuarioActual();

        if (vendedorActual.getRole() != Role.VENDEDOR) {
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
        Usuario vendedorActual = getUsuarioActual();

        if (vendedorActual.getRole() != Role.VENDEDOR) {
            throw new ProductoException.AccesoProhibidoException("Solo vendedores pueden eliminar productos");
        }

        var productoOpt = productoService.obtenerPorId(id);

        if (productoOpt.isEmpty()) {
            throw new ProductoException.NoEncontradoException(id);
        }

        ProductoBD producto = productoOpt.get();

        // Verificar producto pertenece al vendedor loguado
        if (!producto.getVendedor().getId().equals(vendedorActual.getId())) {
            throw new ProductoException.AccesoProhibidoException("No puedes eliminar productos de otro vendedor");
        }

        productoService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
