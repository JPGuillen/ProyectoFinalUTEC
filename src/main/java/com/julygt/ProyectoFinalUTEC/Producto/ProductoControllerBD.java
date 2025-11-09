package com.julygt.ProyectoFinalUTEC.Producto;

import com.julygt.ProyectoFinalUTEC.categorias.Categoria;
import com.julygt.ProyectoFinalUTEC.usuario.Role;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/productosBD")
@RequiredArgsConstructor
public class ProductoControllerBD {

    private final ProductoServiceBD productoServiceBD;
    private final UsuarioRepository usuarioRepository;
    //private final CategoriaRepository categoriaRepository;
    private final AlmacenamientoService almacenamientoService;

    // LISTAR PRODUCTOS (cliente → todos, vendedor → solo suyos)
    @GetMapping
    public ResponseEntity<?> listarProductos(Authentication auth) {
        try {
            if (auth == null) {
                throw new RuntimeException("No estás autenticado.");
            }

            Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

            List<ProductoBD> productos = productoServiceBD.listarTodos();

            List<ProductoDTO> dtoList = productos.stream()
                    .map(p -> {
                        if (usuario.getRole() == Role.VENDEDOR &&
                                p.getVendedor() != null &&
                                p.getVendedor().getId() != null &&
                                p.getVendedor().getId().equals(usuario.getId())) {
                            return ProductoDTO.fromEntityVendedor(p);
                        } else {
                            return ProductoDTO.fromEntityCliente(p);
                        }
                    })
                    .filter(p -> p != null)
                    .toList();

            return ResponseEntity.ok(dtoList);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Error interno al listar productos",
                    "detalle", e.getMessage()
            ));
        }
    }

    // OBTENER PRODUCTO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id, Authentication auth) {
        if (auth == null) {
            throw new RuntimeException("No estás autenticado.");
        }

        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        ProductoBD producto = productoServiceBD.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        ProductoDTO dto = (usuario.getRole() == Role.VENDEDOR
                && producto.getVendedor() != null
                && producto.getVendedor().getId().equals(usuario.getId()))
                ? ProductoDTO.fromEntityVendedor(producto)
                : ProductoDTO.fromEntityCliente(producto);

        return ResponseEntity.ok(dto);
    }

    // BUSCAR PRODUCTOS POR NOMBRE
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<ProductoDTO>> buscarPorNombre(@PathVariable String nombre, Authentication auth) {
        if (auth == null) {
            throw new RuntimeException("No estás autenticado.");
        }

        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        List<ProductoBD> productos = productoServiceBD.buscarPorNombre(nombre);

        List<ProductoDTO> dtoList = productos.stream()
                .map(p -> {
                    if (usuario.getRole() == Role.VENDEDOR && p.getVendedor() != null
                            && p.getVendedor().getId().equals(usuario.getId())) {
                        return ProductoDTO.fromEntityVendedor(p);
                    } else {
                        return ProductoDTO.fromEntityCliente(p);
                    }
                })
                .toList();

        return ResponseEntity.ok(dtoList);
    }


    // CREAR PRODUCTO (solo VENDEDOR)
    @PostMapping("/crear")
    public ResponseEntity<?> crearProducto(
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam("idCategoria") Long idCategoria,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            Authentication auth) {

        try {
            if (auth == null) {
                throw new RuntimeException("No estás autenticado.");
            }

            // 🔹 Buscar al usuario autenticado
            Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

            // 🔹 Validar que sea vendedor
            if (usuario.getRole() != Role.VENDEDOR) {
                return ResponseEntity.status(403).body(Map.of("error", "Solo los vendedores pueden crear productos."));
            }

            // 🔹 Obtener la categoría desde el servicio
            Categoria categoria = productoServiceBD.obtenerCategoriaPorId(idCategoria)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

            // 🔹 Guardar imagen si existe
            String imagenUrl = null;
            if (imagen != null && !imagen.isEmpty()) {
                imagenUrl = almacenamientoService.guardarImagen(imagen);
            }

            // 🔹 Crear el producto
            ProductoBD nuevoProducto = new ProductoBD();
            nuevoProducto.setNombre(nombre);
            nuevoProducto.setPrecio(precio);
            nuevoProducto.setStock(stock);
            nuevoProducto.setCategoria(categoria);
            nuevoProducto.setVendedor(usuario);
            nuevoProducto.setImagenUrl(imagenUrl);

            // 🔹 Guardar el producto
            ProductoBD guardado = productoServiceBD.guardar(nuevoProducto);

            // 🔹 Crear DTO de respuesta evitando NullPointer
            ProductoDTO respuesta = new ProductoDTO();
            respuesta.setId(guardado.getId());
            respuesta.setNombre(guardado.getNombre());
            respuesta.setPrecio(guardado.getPrecio());
            respuesta.setStock(guardado.getStock());
            respuesta.setImagenUrl(guardado.getImagenUrl());
            respuesta.setNombreCategoria(categoria != null ? categoria.getNombre() : "Sin categoría");
            respuesta.setNombreVendedor(
                    usuario != null
                            ? (usuario.getNombrePublicoTienda() != null
                            ? usuario.getNombrePublicoTienda()
                            : usuario.getUsername())
                            : "Sin vendedor"
            );

            System.out.println("✅ Producto guardado correctamente: " + guardado);

            return ResponseEntity.status(201).body(Map.of(
                    "mensaje", "Producto registrado correctamente",
                    "producto", respuesta
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Ocurrió un error al registrar el producto",
                    "detalle", e.getMessage()
            ));
        }
    }


    // ACTUALIZAR PRODUCTO (VENDEDOR)
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            Authentication auth) {
        try {
            if (auth == null) {
                return ResponseEntity.status(401).body(Map.of("error", "No estás autenticado."));
            }

            // Buscar usuario autenticado
            Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

            // Buscar producto
            ProductoBD producto = productoServiceBD.obtenerPorId(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

            // Validar que el vendedor sea el dueño
            if (usuario.getRole() == Role.VENDEDOR &&
                    !producto.getVendedor().getId().equals(usuario.getId())) {
                return ResponseEntity.status(403).body(Map.of("error", "No tienes permiso para modificar este producto."));
            }

            // ✅ Actualiza solo los campos que vienen en el body
            if (body.containsKey("nombre")) {
                producto.setNombre((String) body.get("nombre"));
            }

            if (body.containsKey("precio")) {
                Object precio = body.get("precio");
                if (precio != null) {
                    producto.setPrecio(Double.valueOf(precio.toString()));
                }
            }

            if (body.containsKey("stock")) {
                Object stock = body.get("stock");
                if (stock != null) {
                    producto.setStock(Integer.valueOf(stock.toString()));
                }
            }

            if (body.containsKey("idCategoria")) {
                Long idCategoria = Long.valueOf(body.get("idCategoria").toString());
                Categoria categoria = productoServiceBD.obtenerCategoriaPorId(idCategoria)
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));
                producto.setCategoria(categoria);
            }

            // Guardar cambios
            ProductoBD actualizado = productoServiceBD.guardar(producto);

            // Respuesta final
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Producto actualizado correctamente",
                    "id", actualizado.getId(),
                    "nombre", actualizado.getNombre(),
                    "precio", actualizado.getPrecio(),
                    "stock", actualizado.getStock()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "error", "INTERNAL_ERROR",
                    "message", e.getMessage(),
                    "status", 500
            ));
        }
    }

    // ELIMINAR PRODUCTO (VENDEDOR)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id, Authentication auth) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        ProductoBD producto = productoServiceBD.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        if (!producto.getVendedor().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403).body(Map.of("error", "No tienes permiso para eliminar este producto."));
        }

        productoServiceBD.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente."));
    }
}
