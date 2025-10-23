package com.julygt.ProyectoFinalUTEC.auth;

import com.julygt.ProyectoFinalUTEC.usuario.Usuario;
import com.julygt.ProyectoFinalUTEC.usuario.Role;
import com.julygt.ProyectoFinalUTEC.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthDTO.AuthResponse register(AuthDTO.RegisterRequest request) {

        // Validar si el usuario o email ya existen
        if (usuarioService.existsByUsername(request.getUsername())) {
            throw new AuthException.UserAlreadyExistsException("El nombre de usuario ya existe");
        }

        if (usuarioService.existsByEmail(request.getEmail())) {
            throw new AuthException.UserAlreadyExistsException("El email ya está registrado");
        }

        // Crear entidad Usuario
        var usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());

        // Guardar la contraseña tal como viene (sin encriptar aquí)
        usuario.setContrasena(request.getContrasena());

        // Validar y asignar rol
        if (request.getRole() == null) {
            throw new AuthException.InvalidDataException("El rol es obligatorio");
        }
        usuario.setRole(request.getRole());

        // Ajuste para clientes: nombrePublicoTienda debe ser null
        if (request.getRole() == Role.CLIENTE) {
            usuario.setNombrePublicoTienda(null);
        }

        // Validación condicional: vendedores deben tener nombre de tienda
        if (request.getRole() == Role.VENDEDOR) {
            if (request.getNombreTienda() == null || request.getNombreTienda().isBlank()) {
                throw new AuthException.InvalidDataException(
                        "El nombre de la tienda es obligatorio para vendedores"
                );
            }
            usuario.setNombrePublicoTienda(request.getNombreTienda());
        }

        // Guardar usuario en DB
        usuario = usuarioService.save(usuario);

        // Generar JWT
        var jwtToken = jwtService.generateToken(usuario);

        return new AuthDTO.AuthResponse(
                jwtToken,
                "Bearer",
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRole().name(),
                usuario.getNombrePublicoTienda(),
                usuario.getFechaRegistro()
        );
    }

    public AuthDTO.AuthResponse authenticate(AuthDTO.LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getContrasena()
                    )
            );

            var usuario = (Usuario) authentication.getPrincipal();
            var jwtToken = jwtService.generateToken(usuario);

            return new AuthDTO.AuthResponse(
                    jwtToken,
                    "Bearer",
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getEmail(),
                    usuario.getRole().name(),
                    usuario.getNombrePublicoTienda(),
                    usuario.getFechaRegistro()
            );
        } catch (BadCredentialsException e) {
            throw new AuthException.InvalidCredentialsException();
        }
    }
}
