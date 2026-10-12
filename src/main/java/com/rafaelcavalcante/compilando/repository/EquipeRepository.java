package com.rafaelcavalcante.compilando.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelcavalcante.compilando.model.Equipe;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {
    
}
