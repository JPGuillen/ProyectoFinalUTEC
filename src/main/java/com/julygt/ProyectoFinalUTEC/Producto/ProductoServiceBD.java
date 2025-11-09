package com.julygt.ProyectoFinalUTEC.Producto;

import com.julygt.ProyectoFinalUTEC.categorias.Categoria;
import com.julygt.ProyectoFinalUTEC.categorias.CategoriaRepository;
import com.julygt.ProyectoFinalUTEC.notificaciones.NotificacionService; // Agregado
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceBD {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository; // Inyectamos categoría
    private final NotificacionService notificacionService; //Servicio para enviar notificaciones al vendedor

    // Es inyeccion x medio de constructor  //@RequiredArgsConstructor, @Autowired
    public ProductoServiceBD(ProductoRepository productoRepository,
                             CategoriaRepository categoriaRepository,
                             NotificacionService notificacionService) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.notificacionService = notificacionService; // Inyectamos el servicio de notificaciones
    }

    public List<ProductoBD> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<ProductoBD> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public ProductoBD guardar(ProductoBD producto) {
        // Antes de guardar, verificamos si el producto se quedó sin stock
        // se notifica al vendedor
        ProductoBD guardado = productoRepository.save(producto);

        if (guardado.getStock() <= 0 && guardado.getVendedor() != null) {
            notificacionService.crearNotificacion(
                    guardado.getVendedor().getId(),
                    "Tu producto '" + guardado.getNombre() +
                            "' se ha quedado sin stock. Considera reponerlo para no perder ventas."
            );
        }
        return guardado;
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    // metodo buscarx nombre
    public List<ProductoBD> buscarPorNombre(String nombre) {
        return productoRepository.buscarPorNombre(nombre);
    }

    public List<ProductoBD> listarPorVendedor(Long idVendedor) {
        return productoRepository.findByVendedorId(idVendedor);
    }

    public List<ProductoBD> buscarPorNombreYVendedor(String nombre, Long idVendedor) {
        return productoRepository.findByNombreContainingIgnoreCaseAndVendedorId(nombre, idVendedor);
    }

    // NUEVO: obtener categoría por id
    public Optional<Categoria> obtenerCategoriaPorId(Long idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

}
