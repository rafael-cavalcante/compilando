package com.rafaelcavalcante.compilando.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rafaelcavalcante.compilando.model.Desafio;
import com.rafaelcavalcante.compilando.model.enums.StatusDesafio;
import com.rafaelcavalcante.compilando.repository.DesafioRepository;

@Service 
public class DesafioService {
    private final DesafioRepository desafioRepository;

    public DesafioService(DesafioRepository desafioRepository) {
        this.desafioRepository = desafioRepository;
    }

    // Metodo para salvar um desafio
    public Desafio criarDesafio(Desafio desafio) {
        return this.desafioRepository.save(desafio);
    }

    // Metodo para buscar desafios de um organizador
    public Desafio buscarDesafioPorId(Long id) {
        return this.desafioRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Desafio não encontrado com ID: " + id));
    }

    public List<Desafio> buscarDesafiosPorStatusPublicos() {
        return this.desafioRepository.findByStatusOrderByCriadoEmDesc(StatusDesafio.PUBLICADO);
    }

    public List<Desafio> buscarDesafiosPorOrganizador(Long organizadorId) {
        return this.desafioRepository.findByOrganizadorIdOrderByCriadoEmDesc(organizadorId);
    }

    // Metodo para contar desafios
    public int totalDesafios() {
        return (int) this.desafioRepository.count();
    }

    public int totalDesafiosPublicados() {
        return (int) this.desafioRepository.countByStatus(StatusDesafio.PUBLICADO);
    }
}