package com.rafaelcavalcante.compilando.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelcavalcante.compilando.model.Desafio;
import com.rafaelcavalcante.compilando.model.enums.StatusDesafio;

public interface DesafioRepository extends JpaRepository<Desafio, Long> {
    long countByStatus(StatusDesafio status);

    List<Desafio> findByStatusOrderByCriadoEmDesc(StatusDesafio status);

    List<Desafio> findByOrganizadorIdOrderByCriadoEmDesc(Long organizadorId);

}