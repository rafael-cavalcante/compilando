package com.rafaelcavalcante.compilando.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.rafaelcavalcante.compilando.model.Usuario;
import com.rafaelcavalcante.compilando.model.enums.PapelUsuario;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String tratarEntidadeNaoEncontrada(@AuthenticationPrincipal Usuario usuario, Model model) {
        configurarErro(
                model,
                verificarUsuario(usuario),
                404,
                "Recurso não encontrado",
                "O registro solicitado não existe ou não está mais disponível.",
                "bi-search");

        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String tratarAcessoNegado(@AuthenticationPrincipal Usuario usuario, Model model) {
        configurarErro(
                model,
                verificarUsuario(usuario),
                403,
                "Acesso negado",
                "Você não possui permissão para acessar este recurso.",
                "bi-shield-lock");

        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String tratarErroInterno(@AuthenticationPrincipal Usuario usuario, Model model) {
        configurarErro(
                model,
                verificarUsuario(usuario),
                500,
                "Erro interno do servidor",
                "Ocorreu um erro inesperado ao processar sua solicitação. Tente novamente mais tarde.",
                "bi-exclamation-triangle");

        return "error";
    }

    private void configurarErro(
            Model model,
            String usuario,
            int codigo,
            String titulo,
            String mensagem,
            String icone) {

        model.addAttribute("urlUsuario", usuario);
        model.addAttribute("codigo", codigo);
        model.addAttribute("titulo", titulo);
        model.addAttribute("mensagem", mensagem);
        model.addAttribute("icone", icone);
    }

    private String verificarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getPapel() == null) {
            return "entrar";
        }

        if (usuario.getPapel() == PapelUsuario.ORGANIZADOR) {
            return "organizador";
        }

        return "equipe";
    }
}