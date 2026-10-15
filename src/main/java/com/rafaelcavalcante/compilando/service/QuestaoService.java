package com.rafaelcavalcante.compilando.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.rafaelcavalcante.compilando.model.Questao;
import com.rafaelcavalcante.compilando.repository.QuestaoRepository;

import jakarta.persistence.EntityNotFoundException;
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

    public Questao buscarQuestao(Long questaoId, Long usuarioId){
        Questao questao = this.questaoRepository.findById(questaoId)
                .orElseThrow(() -> new EntityNotFoundException("Questão não encontrado!"));

        if(!questao.getAutor().getId().equals(usuarioId)){
            throw new AccessDeniedException(
                    "Você não tem permissão para acessar esta questão.");
        }

        return questao;
    }

    // Método para listar todas as questões de um desafio específico
    public List<Questao> listarQuestoesPorDesafioId(Long desafioId) {
        return this.questaoRepository.findByDesafioId(desafioId);
    }

    public Page<Questao> listarQuestoes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.questaoRepository.findAll(pageable);
    }

    /**
     * Método para excluir uma Questão
     * @param questaoId
     * @param usuarioId
     */
    @Transactional
    public void excluirQuestao(Long questaoId, Long usuarioId) {
        Questao questao = this.questaoRepository.findById(questaoId)
                .orElseThrow(() -> new EntityNotFoundException("Questão não encontrado!"));

        if (!questao.getAutor().getId().equals(usuarioId)) {
            throw new AccessDeniedException(
                    "Você não tem permissão para excluir esta questão.");
        }
        
        this.questaoRepository.delete(questao);
    }
}
