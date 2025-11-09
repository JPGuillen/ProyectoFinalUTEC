package com.julygt.ProyectoFinalUTEC.carrito;

import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.usuario.UsuarioRepository;
import com.julygt.ProyectoFinalUTEC.usuario.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final UsuarioRepository usuarioRepository;

    public CarritoController(CarritoService carritoService, UsuarioRepository usuarioRepository) {
        this.carritoService = carritoService;
        this.usuarioRepository = usuarioRepository;
    }

    // Método auxiliar — valida que sea CLIENTE
    private void validarCliente(Authentication auth) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new CarritoException("Usuario no encontrado."));
        if (usuario.getRole() != Role.CLIENTE) {
            throw new CarritoException("Solo los clientes pueden acceder al carrito.");
        }
    }

    // Obtener carrito del usuario logueado
    @GetMapping
    public ResponseEntity<CarritoDTO> obtenerCarrito(Authentication authentication) {
        validarCliente(authentication);
        CarritoDTO carrito = carritoService.obtenerCarrito(authentication);
        return ResponseEntity.ok(carrito);
    }

    // Agregar producto al carrito
    @PostMapping("/agregar")
    public ResponseEntity<CarritoDTO> agregarProducto(
            Authentication authentication,
            @RequestParam Long productoId,
            @RequestParam int cantidad
    ) {
        validarCliente(authentication);
        CarritoDTO carritoActualizado = carritoService.agregarProducto(authentication, productoId, cantidad);
        return ResponseEntity.ok(carritoActualizado);
    }

    // Actualizar cantidad de un producto
    @PutMapping("/actualizar")
    public ResponseEntity<CarritoDTO> actualizarCantidad(
            Authentication authentication,
            @RequestParam Long productoId,
            @RequestParam int cantidad
    ) {
        validarCliente(authentication);
        CarritoDTO carritoActualizado = carritoService.actualizarCantidad(authentication, productoId, cantidad);
        return ResponseEntity.ok(carritoActualizado);
    }

    // Disminuir parcialmente cantidad (nuevo)
    @PutMapping("/disminuir")
    public ResponseEntity<CarritoDTO> disminuirCantidad(
            Authentication authentication,
            @RequestParam Long productoId,
            @RequestParam int cantidad
    ) {
        validarCliente(authentication);
        CarritoDTO carritoActualizado = carritoService.disminuirCantidad(authentication, productoId, cantidad);
        return ResponseEntity.ok(carritoActualizado);
    }

    // Eliminar producto completamente
    @DeleteMapping("/eliminar")
    public ResponseEntity<CarritoDTO> eliminarProducto(
            Authentication authentication,
            @RequestParam Long productoId
    ) {
        validarCliente(authentication);
        CarritoDTO carritoActualizado = carritoService.eliminarProducto(authentication, productoId);
        return ResponseEntity.ok(carritoActualizado);
    }
}
