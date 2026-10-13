package com.rafaelcavalcante.compilando.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rafaelcavalcante.compilando.model.Questao;
import com.rafaelcavalcante.compilando.repository.QuestaoRepository;

import jakarta.transaction.Transactional;

@Service 
public class QuestaoService {
    private final QuestaoRepository questaoRepository;
    
    public QuestaoService(QuestaoRepository questaoRepository) {
        this.questaoRepository = questaoRepository;
    }

    /**
     * Método para adicionar uma nova questão
     * @param questao
     * @return O objeto questão salva
     */
    @Transactional 
    public Questao adicionarQuestao(Questao questao) {
        return this.questaoRepository.save(questao);
    }

    // Método para buscar uma questão por ID
    public Questao buscarQuestaoPorId(Long id) {
        return this.questaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Questão não encontrada com o ID: " + id));
    }

    // Método para listar todas as questões de um desafio específico
    public List<Questao> listarQuestoesPorDesafioId(Long desafioId) {
        return this.questaoRepository.findByDesafioId(desafioId);
    }

    public Page<Questao> listarQuestoes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.questaoRepository.findAll(pageable);
    }
}
