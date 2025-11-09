package com.julygt.ProyectoFinalUTEC.carrito;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoBD;
import com.julygt.ProyectoFinalUTEC.Producto.ProductoRepository;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoDetalleRepository detalleRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    // Obtener o crear carrito
    private Carrito obtenerOCrearCarrito(Authentication auth) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new CarritoException.NotFoundException("Usuario no encontrado"));

        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuario(usuario);
                    nuevo.setDetalles(new ArrayList<>());
                    return carritoRepository.save(nuevo);
                });
    }

    // Obtener carrito
    public CarritoDTO obtenerCarrito(Authentication auth) {
        Carrito carrito = obtenerOCrearCarrito(auth);
        return convertirACarritoDTO(carrito);
    }

    // Agregar producto
    public CarritoDTO agregarProducto(Authentication auth, Long productoId, int cantidad) {
        Carrito carrito = obtenerOCrearCarrito(auth);

        ProductoBD producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new CarritoException.NotFoundException("Producto no encontrado"));

        CarritoDetalle detalleExistente = carrito.getDetalles().stream()
                .filter(d -> d.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);

        if (detalleExistente != null) {
            detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
        } else {
            CarritoDetalle nuevoDetalle = new CarritoDetalle();
            nuevoDetalle.setCarrito(carrito);
            nuevoDetalle.setProducto(producto);
            nuevoDetalle.setCantidad(cantidad);
            carrito.getDetalles().add(nuevoDetalle);
        }

        carritoRepository.save(carrito);
        return convertirACarritoDTO(carrito);
    }

    // Actualizar cantidad total
    public CarritoDTO actualizarCantidad(Authentication auth, Long productoId, int nuevaCantidad) {
        Carrito carrito = obtenerOCrearCarrito(auth);

        CarritoDetalle detalle = carrito.getDetalles().stream()
                .filter(d -> d.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new CarritoException.NotFoundException("Producto no está en el carrito"));

        detalle.setCantidad(nuevaCantidad);
        carritoRepository.save(carrito);
        return convertirACarritoDTO(carrito);
    }

    // Disminuir parcialmente cantidad (nuevo)
    public CarritoDTO disminuirCantidad(Authentication auth, Long productoId, int cantidad) {
        Carrito carrito = obtenerOCrearCarrito(auth);

        CarritoDetalle detalle = carrito.getDetalles().stream()
                .filter(d -> d.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new CarritoException.NotFoundException("Producto no está en el carrito"));

        int nuevaCantidad = detalle.getCantidad() - cantidad;
        if (nuevaCantidad <= 0) {
            carrito.getDetalles().remove(detalle); // elimina si queda en 0
        } else {
            detalle.setCantidad(nuevaCantidad);
        }

        carritoRepository.save(carrito);
        return convertirACarritoDTO(carrito);
    }

    // Eliminar completamente un producto
    public CarritoDTO eliminarProducto(Authentication auth, Long productoId) {
        Carrito carrito = obtenerOCrearCarrito(auth);

        carrito.getDetalles().removeIf(detalle ->
                detalle.getProducto().getId().equals(productoId)
        );

        carritoRepository.save(carrito);
        return convertirACarritoDTO(carrito);
    }

    // Conversión a DTO
    private CarritoDTO convertirACarritoDTO(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId_carrito(carrito.getId());
        dto.setId_cliente(carrito.getUsuario().getId());
        dto.setNombreCliente(carrito.getUsuario().getUsername());

        List<CarritoDTO.DetalleDTO> detallesDTO = carrito.getDetalles().stream()
                .map(detalle -> new CarritoDTO.DetalleDTO(
                        detalle.getId(),
                        detalle.getProducto().getNombre(),
                        detalle.getProducto().getPrecio(),
                        detalle.getCantidad()
                ))
                .toList();

        dto.setDetalles(detallesDTO);
        return dto;
    }
}
