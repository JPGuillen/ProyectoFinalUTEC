package com.julygt.ProyectoFinalUTEC.Producto;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class AlmacenamientoService {

    // Ruta base donde se guardarán las imágenes localmente
    private static final String RUTA_BASE = "src/main/resources/static/uploads/";

    public String guardarImagen(MultipartFile archivo) {
        try {
            // Verificar que la carpeta exista, si no, crearla
            Path carpeta = Paths.get(RUTA_BASE);
            if (!Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }

            Path rutaArchivo = carpeta.resolve(archivo.getOriginalFilename());  // ruta destino del archivo

            Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);  // Copia el archivo (si existe, reemplaza)

            // Devuelve URL accesible desde frontend (ejemplo: http://localhost:8080/uploads/nombre.jpg)
            return "/uploads/" + archivo.getOriginalFilename();

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen: " + e.getMessage(), e);
        }
    }
}
