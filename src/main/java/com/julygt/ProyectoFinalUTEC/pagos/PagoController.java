package com.julygt.ProyectoFinalUTEC.pagos;

import com.julygt.ProyectoFinalUTEC.usuario.Role;
import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.util.Map;

@RestController
@RequestMapping("api/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final UsuarioRepository usuarioRepository;

    public PagoController(PagoService pagoService,UsuarioRepository usuarioRepository) {
        this.pagoService = pagoService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
        public ResponseEntity<Map<String, Object>> crearPago(@Valid @RequestBody PagoDTO dto, Authentication auth)
        {

            Usuario usuario = usuarioRepository.findByUsername(auth.getName())   // Validacion Usuario
                    .orElseThrow(() -> new PagoException.PagoFallidoException("Usuario no encontrado."));

            if (usuario.getRole() != Role.CLIENTE) {                            // Valida ROL
                throw new PagoException.PagoFallidoException("Solo clientes pueden realizar pagos.");
            }

            Pago pagoCreado = pagoService.crearPago(dto);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Pago registrado correctamente",
                    "id_pago", pagoCreado.getId()
            ));
        }
}
