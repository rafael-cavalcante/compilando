package com.rafaelcavalcante.compilando.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rafaelcavalcante.compilando.model.Usuario;
import com.rafaelcavalcante.compilando.repository.DesafioRepository;
import com.rafaelcavalcante.compilando.repository.EquipeRepository;

@Controller
public class OrganizadorController {
    private final DesafioRepository desafioRepository;
    private final EquipeRepository equipeRepository;

    public OrganizadorController(DesafioRepository desafioRepository, EquipeRepository equipeRepository) {
        this.desafioRepository = desafioRepository;
        this.equipeRepository = equipeRepository;
    }

    @GetMapping("/organizador")
    public String home(@AuthenticationPrincipal Usuario usuario, Model model) {

        model.addAttribute("nomeOrganizador", usuario.getUsername());

        model.addAttribute("desafios", this.desafioRepository.findAll());

        model.addAttribute("totalDesafios", this.desafioRepository.count());

        model.addAttribute("totalPublicados", this.desafioRepository.countByPublicadoTrue());

        model.addAttribute("totalEquipes", equipeRepository.count());

        return "organizador/home";
    }
}
