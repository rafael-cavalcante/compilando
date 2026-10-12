package com.rafaelcavalcante.compilando.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rafaelcavalcante.compilando.model.Usuario;
import com.rafaelcavalcante.compilando.model.enums.PapelUsuario;
import com.rafaelcavalcante.compilando.repository.UsuarioRepository;

@Service 
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    // Metodo para adicionar um usuário
    public Usuario criarUsuario(Usuario usuario) {
        return this.usuarioRepository.save(usuario);
    }

    // Metodo para buscar um usuário
    public Usuario buscarUsuarioPorId(Long id) {
        return this.usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com ID: " + id));
    }

    public Usuario buscarUsuarioPorUsername(String username) {
        return this.usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com username: " + username));
    }

    public Optional<Usuario> pesquisarUsuarioPorUsername(String username) {
        return this.usuarioRepository.findByUsername(username);
    }

    public int contarUsuariosPorPapel(PapelUsuario papel) {
        return this.usuarioRepository.countByPapel(papel);
    }

    public boolean existeUsuarioPorUsername(String username) {
        return this.usuarioRepository.existsByUsername(username);
    }
}