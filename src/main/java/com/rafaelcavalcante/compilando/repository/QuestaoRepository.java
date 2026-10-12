package com.rafaelcavalcante.compilando.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rafaelcavalcante.compilando.model.Questao;

public interface QuestaoRepository extends JpaRepository<Questao, Long> {

        @Query("select associacao.questao from DesafioQuestao associacao "
            + "where associacao.desafio.id = :desafioId order by associacao.ordem")
        List<Questao> findByDesafioId(@Param("desafioId") Long desafioId);
    
}
