package com.rafaelcavalcante.compilando.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.rafaelcavalcante.compilando.model.Desafio;
import com.rafaelcavalcante.compilando.model.enums.StatusDesafio;
import com.rafaelcavalcante.compilando.repository.DesafioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DesafioService {
    private final DesafioRepository desafioRepository;

    public DesafioService(DesafioRepository desafioRepository) {
        this.desafioRepository = desafioRepository;
    }

    /**
     * Método para salvar um desafio
     * @param desafio
     * @return O objeto desafio salvo
     */
    @Transactional
    public Desafio criarDesafio(Desafio desafio) {
        return this.desafioRepository.save(desafio);
    }

    public Desafio buscarDesafio(Long desafioId, Long usuarioId){
        Desafio desafio = this.desafioRepository.findById(desafioId)
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado!"));

        if(!desafio.getOrganizador().getId().equals(usuarioId)){
            throw new AccessDeniedException(
                    "Você não tem permissão para acessar este desafio.");
        }

        return desafio;
    }

    /**
     * Método para listar desafios
     * @param organizadorId
     * @param numeroPagina
     * @param tamanhoPagina
     * @return Uma página com até 9 desafios
     */
    public Page<Desafio> listarDesafios(Long organizadorId, int numeroPagina, int tamanhoPagina) {
        Pageable paginacao = PageRequest.of(numeroPagina, tamanhoPagina);
        return this.desafioRepository.findAllByOrganizadorId(organizadorId, paginacao);
    }

    /**
     * Método para exluir um desafio
     * @param desafioId
     * @param usuarioId
     */
    @Transactional
    public void excluirDesafio(Long desafioId, Long usuarioId) {
        Desafio desafio = this.desafioRepository.findById(desafioId)
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado!"));

        if (!desafio.getOrganizador().getId().equals(usuarioId)) {
            throw new AccessDeniedException(
                    "Você não tem permissão para excluir este desafio.");
        }
        
        this.desafioRepository.delete(desafio);
    }

    /**
     * Método para listar top 3 desafios
     * @param organizadorId
     * @return Listar o top 3 desafios do organizador
     */
    public List<Desafio> listarDesafios(Long organizadorId) {
        return this.desafioRepository.findTop3ByOrganizadorIdOrderByCriadoEmDesc(organizadorId);
    }

    // Metodo para buscar desafios de um organizador
    public Desafio buscarDesafioPorId(Long id) {
        return this.desafioRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Desafio não encontrado com ID: " + id));
    }

    // Metodo para listar desafios publicados
    public List<Desafio> listarDesafiosPorStatusPublico() {
        return this.desafioRepository.findByStatusOrderByCriadoEmDesc(StatusDesafio.PUBLICADO);
    }

    // Metodo para listar todos os desafios
    public Page<Desafio> listarDesafios(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.desafioRepository.findAll(pageable);
    }

    // Metodo para contar desafios
    public int totalDesafios() {
        return (int) this.desafioRepository.count();
    }

    public int totalDesafiosPublicados() {
        return (int) this.desafioRepository.countByStatus(StatusDesafio.PUBLICADO);
    }
}