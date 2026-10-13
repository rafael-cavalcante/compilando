package com.rafaelcavalcante.compilando.service;

import org.springframework.stereotype.Service;

import com.rafaelcavalcante.compilando.model.DesafioQuestao;
import com.rafaelcavalcante.compilando.repository.DesafioQuestaoRepository;

@Service 
public class DesafioQuestaoService {
    private final DesafioQuestaoRepository desafioQuestaoRepository;

    public DesafioQuestaoService(DesafioQuestaoRepository desafioQuestaoRepository) {
        this.desafioQuestaoRepository = desafioQuestaoRepository;
    }

    public DesafioQuestao salvarDesafioQuestao(DesafioQuestao desafioQuestao) {
        return this.desafioQuestaoRepository.save(desafioQuestao);
    }

    public DesafioQuestao buscarMaiorOrdemPorDesafio(Long desafioId) {
        return this.desafioQuestaoRepository.findFirstByDesafioIdOrderByOrdemDesc(desafioId);
    }
}