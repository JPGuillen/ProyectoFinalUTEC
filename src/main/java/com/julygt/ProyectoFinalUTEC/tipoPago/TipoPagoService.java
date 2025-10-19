package com.julygt.ProyectoFinalUTEC.tipoPago;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TipoPagoService {

    private final TipoPagoRepository tipoPagoRepository;

    public TipoPagoService(TipoPagoRepository tipoPagoRepository) {
        this.tipoPagoRepository = tipoPagoRepository;
    }

    public List<TipoPago> listarTodos() { return tipoPagoRepository.findAll(); }

    public Optional<TipoPago> obtenerPorId(Long id) { return tipoPagoRepository.findById(id); }

    public TipoPago guardar(TipoPago tipoPago) { return tipoPagoRepository.save(tipoPago); }

    public void eliminar(Long id) { tipoPagoRepository.deleteById(id); }
}
