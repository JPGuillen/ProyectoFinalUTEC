package com.julygt.ProyectoFinalUTEC.tipoPago;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipo_pago")
public class TipoPagoController {

    private final TipoPagoService tipoPagoService;

    // Constructor
    public TipoPagoController(TipoPagoService tipoPagoService) {
        this.tipoPagoService = tipoPagoService;
    }

    // Listar todos los tipos de pago
    @GetMapping
    public List<TipoPago> listar() {
        return tipoPagoService.listarTodos();
    }

    // Obtener tipo de pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoPago> obtener(@PathVariable Long id) {
        return tipoPagoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo tipo de pago
    @PostMapping
    public TipoPago crear(@RequestBody TipoPago tipoPago) {
        return tipoPagoService.guardar(tipoPago);
    }

    // Actualizar un tipo de pago existente
    @PutMapping("/{id}")
    public ResponseEntity<TipoPago> actualizar(@PathVariable Long id, @RequestBody TipoPago tipoPago) {
        return tipoPagoService.obtenerPorId(id)
                .map(t -> {
                    t.setMetodo(tipoPago.getMetodo());
                    t.setMonto(tipoPago.getMonto());
                    t.setEstadoPago(tipoPago.getEstadoPago());
                    t.setPedido(tipoPago.getPedido()); // Relación ManyToOne con Pedido
                    return ResponseEntity.ok(tipoPagoService.guardar(t));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un tipo de pago
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (tipoPagoService.obtenerPorId(id).isPresent()) {
            tipoPagoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
