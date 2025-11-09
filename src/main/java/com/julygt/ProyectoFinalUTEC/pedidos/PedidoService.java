package com.julygt.ProyectoFinalUTEC.pedidos;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoBD;
import com.julygt.ProyectoFinalUTEC.Producto.ProductoRepository;
import com.julygt.ProyectoFinalUTEC.carrito.*;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.julygt.ProyectoFinalUTEC.notificaciones.NotificacionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoDetalleRepository pedidoDetalleRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;


    // Crear un pedido a partir del carrito
    public Pedido crearPedidoDesdeCarrito(Authentication auth, String direccionEnvio) {
        Usuario cliente = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new PedidoException.NotFoundException("Usuario no encontrado."));

        // Buscar carrito
        Carrito carrito = carritoRepository.findByUsuario(cliente)
                .orElseThrow(() -> new PedidoException.NotFoundException("No se encontró el carrito del usuario."));

        if (carrito.getDetalles() == null || carrito.getDetalles().isEmpty()) {
            throw new PedidoException.BusinessException("No hay productos en el carrito para generar el pedido.");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(cliente);
        pedido.setEstado("pendiente");
        pedido.setDireccionEnvio(direccionEnvio);
        pedido.setFechaCreacion(LocalDateTime.now());

        List<PedidoDetalle> detallesPedido = new ArrayList<>();
        double total = 0.0;

        // Crear detalles del pedido a partir de los del carrito
        for (CarritoDetalle detalleCarrito : carrito.getDetalles()) {
            ProductoBD producto = detalleCarrito.getProducto();

            if (producto == null) {
                throw new PedidoException.BusinessException("Un producto del carrito no existe.");
            }

            // Restar stock
            if (producto.getStock() < detalleCarrito.getCantidad()) {
                throw new PedidoException.BusinessException(
                        "No hay suficiente stock para el producto: " + producto.getNombre()
                );
            }
            producto.setStock(producto.getStock() - detalleCarrito.getCantidad());
            productoRepository.save(producto);

            // Crear detalle de pedido
            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setVendedor(producto.getVendedor()); // asignamos el vendedor
            detalle.setCantidad(detalleCarrito.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            total += producto.getPrecio() * detalleCarrito.getCantidad();
            detallesPedido.add(detalle);
        }

        pedido.setTotal(total);
        pedido.setDetalles(detallesPedido);

        // Guardar pedido principal y sus detalles
        Pedido guardado = pedidoRepository.save(pedido);
        pedidoDetalleRepository.saveAll(detallesPedido);

        // ✅ Envia notificaciones a los vendedores involucrados en el pedido
        List<Usuario> vendedoresNotificados = new ArrayList<>();
        for (PedidoDetalle detalle : detallesPedido) {
            Usuario vendedor = detalle.getVendedor();
            if (vendedor != null && !vendedoresNotificados.contains(vendedor)) {
                notificacionService.crearNotificacion(
                        vendedor.getId(),
                        "Has recibido un nuevo pedido del cliente " + cliente.getUsername()
                );
                vendedoresNotificados.add(vendedor);
            }
        }

// ✅ También puedes notificar al cliente que su pedido fue registrado
        notificacionService.crearNotificacion(
                cliente.getId(),
                "Tu pedido ha sido registrado con éxito y se encuentra en estado pendiente."
        );


        // Vaciar carrito del usuario después de crear el pedido
        carrito.getDetalles().clear();
        carritoRepository.save(carrito);

        return guardado;
    }


    // Lista pedidos
    public List<Pedido> listarPedidosUsuario(Authentication auth) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new PedidoException.NotFoundException("Usuario no encontrado."));
        return pedidoRepository.findByUsuario(usuario);
    }

    // Obtener un pedido por su ID
    public Pedido obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoException.NotFoundException("Pedido no encontrado con ID: " + id));
    }

    public Pedido obtenerPedidoPorIdYUsuario(Long id, Authentication auth) {
        // 1-verificar usuario autenticado
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new PedidoException.NotFoundException("Usuario no encontrado."));

        // 2-obtiene pedido (reutiliza metodo)
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoException.NotFoundException("Pedido no encontrado con ID: " + id));

        // 3) validar propiedad
        /*
        Long pedidoOwnerId = pedido.getUsuario() != null ? pedido.getUsuario().getId() : null;
        if (pedidoOwnerId == null || !pedidoOwnerId.equals(usuario.getId())) {
            // Si prefieres, puedes lanzar NotFoundException para evitar revelar existencia; aquí uso BusinessException
            throw new PedidoException.BusinessException("No tienes permiso para ver este pedido.");
        }
        */
        if (!pedido.getUsuario().getId().equals(usuario.getId())) {
            throw new PedidoException.BusinessException("No tienes permiso para ver este pedido.");
        }

        return pedido;
    }

    // 🔹 Nuevo método: listar pedidos de un vendedor autenticado
    // Se usa en PedidoController para mostrar los pedidos donde haya vendido productos
    public List<Pedido> listarPedidosPorVendedor(Long idVendedor) {
        return pedidoDetalleRepository.findDistinctPedidosByVendedor(idVendedor);
    }

    // 🔹 Nuevo método: actualizar el estado del pedido (se usa al cancelar)
    public void actualizarEstado(Pedido pedido) {
        pedidoRepository.save(pedido);
    }
}
