package com.julygt.ProyectoFinalUTEC.carrito;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public List<CarritoDTO> listar() {
        return carritoService.listarTodos()
                .stream()
                .map(c -> new CarritoDTO(
                        c.getId_carrito(),
                        c.getCliente(),
                        c.getDetalles().stream()
                                .map(d -> new CarritoDTO.DetalleDTO(
                                        d.getId_detalle(),
                                        d.getProducto(),
                                        d.getCantidad()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoDTO> obtener(@PathVariable Long id) {
        return carritoService.obtenerPorId(id)
                .map(c -> new CarritoDTO(
                        c.getId_carrito(),
                        c.getCliente(),
                        c.getDetalles().stream()
                                .map(d -> new CarritoDTO.DetalleDTO(
                                        d.getId_detalle(),
                                        d.getProducto(),
                                        d.getCantidad()
                                ))
                                .collect(Collectors.toList())
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CarritoDTO crear(@RequestBody CarritoDTO dto) {
        Carrito carrito = new Carrito();
        carrito.setCliente(dto.getCliente());

        List<CarritoDetalle> detalles = dto.getDetalles()
                .stream()
                .map(d -> {
                    CarritoDetalle detalle = new CarritoDetalle();
                    detalle.setProducto(d.getProducto());
                    detalle.setCantidad(d.getCantidad());
                    detalle.setCarrito(carrito);
                    return detalle;
                }).toList();

        carrito.setDetalles(detalles);

        Carrito guardado = carritoService.guardar(carrito);
        return new CarritoDTO(
                guardado.getId_carrito(),
                guardado.getCliente(),
                guardado.getDetalles().stream()
                        .map(d -> new CarritoDTO.DetalleDTO(
                                d.getId_detalle(),
                                d.getProducto(),
                                d.getCantidad()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        carritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

