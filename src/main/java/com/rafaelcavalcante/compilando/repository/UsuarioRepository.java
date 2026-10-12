package com.rafaelcavalcante.compilando.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelcavalcante.compilando.model.Usuario;
import com.rafaelcavalcante.compilando.model.enums.PapelUsuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    int countByPapel(PapelUsuario papel);

}