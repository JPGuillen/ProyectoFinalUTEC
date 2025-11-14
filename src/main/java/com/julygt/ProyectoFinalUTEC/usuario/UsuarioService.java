package com.julygt.ProyectoFinalUTEC.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(usernameOrEmail)
                .or(() -> usuarioRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + usernameOrEmail));
    }


    @Transactional
    public Usuario save(Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
