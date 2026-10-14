package com.rafaelcavalcante.compilando.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rafaelcavalcante.compilando.model.Usuario;
import com.rafaelcavalcante.compilando.service.DesafioService;

@Controller 
public class EquipeController {
    private final DesafioService desafioService;

    public EquipeController(DesafioService desafioService) {
        this.desafioService = desafioService;
    }

    @GetMapping("/equipe")
    public String dashboard(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("desafios", this.desafioService.listarDesafiosPorStatusPublico());
        return "equipe/home";
    }

}
