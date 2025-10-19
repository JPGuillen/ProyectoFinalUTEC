
package com.julygt.ProyectoFinalUTEC.pedidos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoDTO> listar() {
        return pedidoService.listarTodos()
                .stream()
                .map(p -> new PedidoDTO(
                        p.getId_pedido(),
                        p.getCliente(),
                        p.getFecha_pedido(),
                        p.getEstado(),
                        p.getDetalles().stream()
                                .map(d -> new PedidoDTO.DetalleDTO(
                                        d.getId_detalle(),
                                        d.getProducto(),
                                        d.getCantidad()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtener(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(p -> new PedidoDTO(
                        p.getId_pedido(),
                        p.getCliente(),
                        p.getFecha_pedido(),
                        p.getEstado(),
                        p.getDetalles().stream()
                                .map(d -> new PedidoDTO.DetalleDTO(
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
    public PedidoDTO crear(@RequestBody PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setCliente(dto.getCliente());
        pedido.setFecha_pedido(LocalDateTime.now());
        pedido.setEstado(dto.getEstado());

        List<PedidoDetalle> detalles = dto.getDetalles()
                .stream()
                .map(d -> {
                    PedidoDetalle detalle = new PedidoDetalle();
                    detalle.setProducto(d.getProducto());
                    detalle.setCantidad(d.getCantidad());
                    detalle.setPedido(pedido);
                    return detalle;
                }).toList();

        pedido.setDetalles(detalles);
        Pedido guardado = pedidoService.guardar(pedido);

        return new PedidoDTO(
                guardado.getId_pedido(),
                guardado.getCliente(),
                guardado.getFecha_pedido(),
                guardado.getEstado(),
                guardado.getDetalles().stream()
                        .map(d -> new PedidoDTO.DetalleDTO(
                                d.getId_detalle(),
                                d.getProducto(),
                                d.getCantidad()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
