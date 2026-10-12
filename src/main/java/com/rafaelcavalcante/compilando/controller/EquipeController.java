package com.rafaelcavalcante.compilando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class EquipeController {

    @GetMapping("/equipe")
    public String dashboard() {
        return "equipe/home";
    }

}
