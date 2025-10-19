package com.julygt.ProyectoFinalUTEC.pedidos;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void eliminar(Long id) {
        pedidoRepository.deleteById(id);
    }
}
