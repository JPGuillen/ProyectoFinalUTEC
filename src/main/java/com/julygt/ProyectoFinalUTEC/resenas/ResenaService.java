package com.julygt.ProyectoFinalUTEC.resenas;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoBD;
import com.julygt.ProyectoFinalUTEC.Producto.ProductoRepository;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Listar reseñas de un producto (para todos)
    public List<ResenaDTO> listarPorProducto(Long idProducto) {
        // Traer la entidad ProductoBD
        ProductoBD producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Buscar reseñas por entidad y mapear a DTO
        return resenaRepository.findByProducto(producto).stream()
                .map(ResenaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Crear nueva reseña (usuario logueado)
    public void crearResena(ResenaDTO resenaDTO, Usuario usuario) {
        ProductoBD producto = productoRepository.findById(resenaDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Resena resena = new Resena();
        resena.setProducto(producto);
        resena.setUsuario(usuario);                     // usar el usuario logueado
        resena.setCalificacion(resenaDTO.getCalificacion());
        resena.setComentario(resenaDTO.getComentario());

        resenaRepository.save(resena);
    }
    public Double obtenerPromedioCalificacion(Long idProducto) {
        return resenaRepository.promedioCalificacionPorProducto(idProducto)
                .orElse(0.0);                   // devuelve 0 si no hay reseñas
    }

}
