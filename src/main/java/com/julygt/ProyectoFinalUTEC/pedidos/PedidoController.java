package com.julygt.ProyectoFinalUTEC.pedidos;

import com.julygt.ProyectoFinalUTEC.usuario.Role;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioRepository usuarioRepository;

    // 🔹 1. Crear pedido desde carrito (solo CLIENTE autenticado)
    @PostMapping("/crear")
    public ResponseEntity<?> crearPedidoDesdeCarrito  (
                                            Authentication auth,
                                    @RequestBody Map <String,String> body) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new PedidoException.NotFoundException("Usuario no encontrado."));

        // Validación: solo CLIENTE puede crear pedidos
        if (usuario.getRole() != Role.CLIENTE) {
            throw new PedidoException.BusinessException("Solo los clientes pueden generar pedidos.");
        }

        String direccionEnvio = body.get("direccionEnvio");
        if (direccionEnvio == null || direccionEnvio.isBlank()) {
            throw new PedidoException.BusinessException("La dirección de envío es obligatoria.");
        }

        Pedido nuevo = pedidoService.crearPedidoDesdeCarrito(auth, direccionEnvio);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Pedido generado correctamente.",
                "id_pedido", nuevo.getId(),
                "total", nuevo.getTotal(),
                "direccion_envio", nuevo.getDireccionEnvio()
        ));
    }

    // 🔹 2. Listar pedidos del usuario autenticado
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoDTO>> listarPedidosUsuario(Authentication auth) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new PedidoException.NotFoundException("Usuario no encontrado."));

        List<Pedido> pedidos = pedidoService.listarPedidosUsuario(auth);
        List<PedidoDTO> pedidosDTO = pedidos.stream()
                .map(PedidoDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(pedidosDTO);
    }

    // 🔹 3. Obtener pedido por ID — CLIENTE solo puede ver los suyos
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedidoPorId(@PathVariable Long id, Authentication auth) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new PedidoException.NotFoundException("Usuario no encontrado."));

        Pedido pedido = pedidoService.obtenerPedidoPorIdYUsuario(id, auth);

        // CLIENTE: solo sus pedidos / VENDEDOR: puede ver si está relacionado a productos suyos
        if (usuario.getRole() == Role.CLIENTE) {
            if (!pedido.getUsuario().getId().equals(usuario.getId())) {
                throw new PedidoException.BusinessException("No puedes acceder a pedidos de otro usuario.");
            }
        }

        return ResponseEntity.ok(PedidoDTO.fromEntity(pedido));
    }

    // 🔹 4. Listar pedidos por vendedor autenticado
    // (Vendedor puede ver pedidos donde haya vendido productos)
    @GetMapping("/vendedor")
    public ResponseEntity<?> listarPedidosPorVendedor(Authentication auth) {
        Usuario vendedor = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new PedidoException.NotFoundException("Usuario no encontrado."));

        if (vendedor.getRole() != Role.VENDEDOR) {
            throw new PedidoException.BusinessException("Solo los vendedores pueden acceder a esta lista.");
        }

        List<Pedido> pedidos = pedidoService.listarPedidosPorVendedor(vendedor.getId());
        List<PedidoDTO> pedidosDTO = pedidos.stream()
                .map(PedidoDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(pedidosDTO);
    }

    // 🔹 5. Cancelar pedido (solo CLIENTE y si está pendiente)
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id, Authentication auth) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new PedidoException.NotFoundException("Usuario no encontrado."));

        if (usuario.getRole() != Role.CLIENTE) {
            throw new PedidoException.BusinessException("Solo los clientes pueden cancelar pedidos.");
        }

        Pedido pedido = pedidoService.obtenerPedidoPorIdYUsuario(id, auth);

        if (!"PENDIENTE".equalsIgnoreCase(pedido.getEstado())) {
            throw new PedidoException.BusinessException("Solo se pueden cancelar pedidos pendientes.");
        }

        pedido.setEstado("CANCELADO");
        pedidoService.actualizarEstado(pedido);

        return ResponseEntity.ok(Map.of("mensaje", "Pedido cancelado correctamente."));
    }
}
