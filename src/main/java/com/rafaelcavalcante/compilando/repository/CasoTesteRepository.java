package com.rafaelcavalcante.compilando.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelcavalcante.compilando.model.CasoTeste;

public interface CasoTesteRepository extends JpaRepository<CasoTeste, Long> {

    List<CasoTeste> findByQuestaoId(Long questaoId);
    
}