package com.rafaelcavalcante.compilando.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rafaelcavalcante.compilando.model.Usuario;

@Controller
public class ErrorController {

    @GetMapping("/acesso-negado")
    public String acessoNegado(@AuthenticationPrincipal Usuario usuario, Model model) {

        model.addAttribute("codigo", 403);
        model.addAttribute("titulo", "Acesso negado");
        model.addAttribute("mensagem", "Você não possui permissão para acessar este recurso.");
        model.addAttribute("icone", "bi-shield-lock");

        String destino = "/entrar";

        if (usuario != null && usuario.getPapel() != null) {
            destino = switch (usuario.getPapel()) {
                case ORGANIZADOR -> "/organizador";
                default -> "/equipe";
            };
        }

        model.addAttribute("urlRetorno", destino);

        return "error";
    }
}