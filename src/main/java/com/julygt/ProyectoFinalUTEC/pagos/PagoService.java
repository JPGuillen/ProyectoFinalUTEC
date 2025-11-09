package com.julygt.ProyectoFinalUTEC.pagos;

import com.julygt.ProyectoFinalUTEC.pedidos.Pedido;
import com.julygt.ProyectoFinalUTEC.pedidos.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;

    public PagoService(PagoRepository pagoRepository, PedidoRepository pedidoRepository) {
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public Pago crearPago(PagoDTO dto) {
        // Buscar pedido por ID
        // PagoException.PedidoNoEncontradoException en lugar de IllegalArgumentException
        Pedido pedido = pedidoRepository.findById(dto.getIdPedido())
                .orElseThrow(() -> new PagoException.PedidoNoEncontradoException(dto.getIdPedido()));

        //  Valida metodo de pago
        // Opcional: si se envía NULL, se lanza una excepción específica
        if (dto.getMetodo() == null) {
            throw new PagoException.MetodoPagoInvalidoException("null");
        }

        // Verifica si el pago existe
        // Si el pedido ya tiene un pago registrado, se lanza PagoFallidoException
        if (pagoRepository.findByPedido(pedido).isPresent()) {
            throw new PagoException.PagoFallidoException(
                    "Este pedido ya tiene un pago registrado, no se puede duplicar."
            );
        }

        // Crea Entidad Pago
        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodo(dto.getMetodo());
        pago.setMonto(dto.getMonto());
        pago.setTelefono(dto.getTelefonoContacto());

        return pagoRepository.save(pago);
    }
}
