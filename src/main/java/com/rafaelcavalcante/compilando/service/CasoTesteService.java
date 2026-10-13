package com.rafaelcavalcante.compilando.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rafaelcavalcante.compilando.model.CasoTeste;
import com.rafaelcavalcante.compilando.repository.CasoTesteRepository;

import jakarta.transaction.Transactional;

@Service
public class CasoTesteService {
    private final CasoTesteRepository casoTesteRepository;

    public CasoTesteService(CasoTesteRepository casoTesteRepository) {
        this.casoTesteRepository = casoTesteRepository;
    }

    /**
     * Método para adicionar um novo caso de teste
     * @param casoTeste
     */
    @Transactional
    public CasoTeste adicionarCasoTeste(CasoTeste casoTeste) {
        return casoTesteRepository.save(casoTeste);
    }

    // Método para listar todos os casos de teste de uma questão específica
    public List<CasoTeste> listarCasosTeste(Long questaoId) {
        return casoTesteRepository.findByQuestaoId(questaoId);
    }

    // Método de paginação para listar todos os casos de teste
    public Page<CasoTeste> listarCasosTeste(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.casoTesteRepository.findAll(pageable);
    }
}