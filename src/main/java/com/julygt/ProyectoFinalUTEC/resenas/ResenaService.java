package com.julygt.ProyectoFinalUTEC.resenas;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    public List<Resena> listarTodas() { return resenaRepository.findAll(); }

    public Optional<Resena> obtenerPorId(Long id) { return resenaRepository.findById(id); }

    public Resena guardar(Resena resena) { return resenaRepository.save(resena); }

    public void eliminar(Long id) { resenaRepository.deleteById(id); }
}


