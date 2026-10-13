package com.rafaelcavalcante.compilando.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rafaelcavalcante.compilando.service.DesafioService;

@Controller
@RequestMapping("/desafios")
public class DesafioController {
    private final DesafioService desafioService;

    public DesafioController(DesafioService desafioService) {
        this.desafioService = desafioService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ORGANIZADOR')")
    public String desafios(Model model) {
        model.addAttribute("desafios", this.desafioService.listarDesafiosPorStatusPublicado());
        return "organizador/desafios";
    }
}