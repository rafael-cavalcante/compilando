package com.rafaelcavalcante.compilando.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelcavalcante.compilando.model.DesafioQuestao;

public interface DesafioQuestaoRepository extends JpaRepository<DesafioQuestao, Long> {
    
    Integer findFirstByDesafioIdOrderByOrdemDesc(Long desafioId);

}
