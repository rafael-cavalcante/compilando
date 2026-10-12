package com.rafaelcavalcante.compilando.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rafaelcavalcante.compilando.model.Questao;
import com.rafaelcavalcante.compilando.repository.QuestaoRepository;

@Service 
public class QuestaoService {
    private final QuestaoRepository questaoRepository;
    
    public QuestaoService(QuestaoRepository questaoRepository) {
        this.questaoRepository = questaoRepository;
    }

    public Questao criarQuestao(Questao questao) {
        return this.questaoRepository.save(questao);
    }

    public List<Questao> listarQuestoesPorDesafioId(Long desafioId) {
        return this.questaoRepository.findByDesafioId(desafioId);
    }
}
