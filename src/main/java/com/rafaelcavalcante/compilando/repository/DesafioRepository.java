package com.rafaelcavalcante.compilando.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelcavalcante.compilando.model.Desafio;
import com.rafaelcavalcante.compilando.model.Organizador;

public interface DesafioRepository extends JpaRepository<Desafio, Long> {
    long countByPublicadoTrue();

    List<Desafio> findByOrganizadorOrderByCriadoEmDesc(
        Organizador organizador
    );

    List<Desafio> findByOrganizadorIdOrderByCriadoEmDesc(
        Long organizadorId
    );
    
}
