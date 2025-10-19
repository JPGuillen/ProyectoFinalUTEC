package com.julygt.ProyectoFinalUTEC.carrito;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;

    public CarritoService(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public List<Carrito> listarTodos() {
        return carritoRepository.findAll();
    }

    public Optional<Carrito> obtenerPorId(Long id) {
        return carritoRepository.findById(id);
    }

    @Transactional
    public Carrito guardar(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Transactional
    public void eliminar(Long id) {
        carritoRepository.deleteById(id);
    }
}
