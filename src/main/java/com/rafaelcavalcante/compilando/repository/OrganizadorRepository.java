package com.rafaelcavalcante.compilando.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelcavalcante.compilando.model.Organizador;

public interface OrganizadorRepository extends JpaRepository<Organizador, Long> {
    
}
