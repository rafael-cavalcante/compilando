package com.rafaelcavalcante.compilando.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rafaelcavalcante.compilando.model.Desafio;
import com.rafaelcavalcante.compilando.model.DesafioQuestao;
import com.rafaelcavalcante.compilando.model.Questao;
import com.rafaelcavalcante.compilando.model.Usuario;
import com.rafaelcavalcante.compilando.model.enums.Nivel;
import com.rafaelcavalcante.compilando.model.enums.PapelUsuario;
import com.rafaelcavalcante.compilando.model.enums.StatusDesafio;
import com.rafaelcavalcante.compilando.service.DesafioQuestaoService;
import com.rafaelcavalcante.compilando.service.DesafioService;
import com.rafaelcavalcante.compilando.service.QuestaoService;
import com.rafaelcavalcante.compilando.service.UsuarioService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/organizador")
public class OrganizadorController {
    private final UsuarioService usuarioService;
    private final DesafioService desafioService;
    private final QuestaoService questaoService;
    private final DesafioQuestaoService desafioQuestaoService;

    public OrganizadorController(UsuarioService usuarioService, DesafioService desafioService, QuestaoService questaoService, DesafioQuestaoService desafioQuestaoService) {
        this.usuarioService = usuarioService;
        this.desafioService = desafioService;
        this.questaoService = questaoService;
        this.desafioQuestaoService = desafioQuestaoService;
    }

    @GetMapping()
    public String home(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("nomeOrganizador", usuario.getUsername());
        model.addAttribute("desafios", this.desafioService.buscarDesafiosPorOrganizador(usuario.getId()));
        model.addAttribute("totalDesafios", this.desafioService.totalDesafios());
        model.addAttribute("totalPublicados", this.desafioService.totalDesafiosPublicados());
        model.addAttribute("totalEquipes", this.usuarioService.contarUsuariosPorPapel(PapelUsuario.EQUIPE));

        return "organizador/home";
    }

    @GetMapping("/desafios")
    public String desafios(Model model) {
        model.addAttribute("totalDesafios", this.desafioService.totalDesafios());
        model.addAttribute("totalPublicados", this.desafioService.totalDesafiosPublicados());
        model.addAttribute("niveis", Nivel.values());
        model.addAttribute("desafio", new Desafio());

        return "organizador/desafios";
    }

    @GetMapping("/desafios/novo")
    public String novoDesafio(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("desafio", new Desafio());
        model.addAttribute("niveis", Nivel.values());
        model.addAttribute("statusDesafio", StatusDesafio.values());
        model.addAttribute("nomeOrganizador", usuario.getUsername());
        return "organizador/desafioForm";
    }

    @PostMapping("/desafios/novo")
    @Transactional
    public String cadastrar(@AuthenticationPrincipal Usuario usuario, @Valid Desafio desafio) {

        // Recupera o organizador autenticado.
        Usuario organizador = this.usuarioService
                .pesquisarUsuarioPorUsername(usuario.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                        "Organizador não encontrado para o usuário autenticado."));

        // Define o responsável no servidor.
        desafio.setOrganizador(organizador);

        // Evita publicar antes de cadastrar as questões.
        desafio.setStatus(StatusDesafio.RASCUNHO);

        // Salva e obtém o ID gerado.
        Desafio desafioSalvo = this.desafioService.criarDesafio(desafio);

        // Encaminha para o gerenciamento das questões.
        return "redirect:/organizador/desafios/"
                + desafioSalvo.getId()
                + "/questoes";
    }

    @GetMapping("/desafios/{id}/questoes")
    public String novaQuestao(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id, Model model) {
        Desafio desafio = this.desafioService.buscarDesafioPorId(id);

        List<Questao> questoes = this.questaoService.listarQuestoesPorDesafioId(desafio.getId());

        model.addAttribute("questao", new Questao());
        model.addAttribute("niveis", Nivel.values());
        model.addAttribute("desafio", desafio);
        model.addAttribute("questoes", questoes);

        return "organizador/questaoForm";

    }

    @PostMapping("/desafios/{id}/questoes/novo")
    @Transactional
    public String adicionarQuestao(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id,
            @Valid Questao questao, Model model, RedirectAttributes redirectAttributes) {

        Usuario autor= this.usuarioService
                .pesquisarUsuarioPorUsername(usuario.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Organizador não encontrado!"));

        Desafio desafio = this.desafioService.buscarDesafioPorId(id);

        if (!desafio.getOrganizador().getId().equals(autor.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não é o organizador deste desafio!");
        }

        Questao novaQuestao = new Questao();
        novaQuestao.setTitulo(questao.getTitulo());
        novaQuestao.setEnunciado(questao.getEnunciado());
        novaQuestao.setFormatoEntrada(questao.getFormatoEntrada());
        novaQuestao.setFormatoSaida(questao.getFormatoSaida());
        novaQuestao.setRestricoes(questao.getRestricoes());
        novaQuestao.setNivel(questao.getNivel());
        novaQuestao.setTempoLimiteMs(questao.getTempoLimiteMs());
        novaQuestao.setMemoriaLimiteMb(questao.getMemoriaLimiteMb());
        novaQuestao.setAutor(autor);

        Questao questaoSalva = this.questaoService.criarQuestao(novaQuestao);

        DesafioQuestao desafioQuestao = new DesafioQuestao();
        desafioQuestao.setDesafio(desafio);
        desafioQuestao.setQuestao(questaoSalva);

        Integer ultimaOrdem = desafioQuestaoService.buscarMaiorOrdemPorDesafio(desafio.getId());

        desafioQuestao.setOrdem(ultimaOrdem == null ? 1 : ultimaOrdem + 1);

        this.desafioQuestaoService.salvarDesafioQuestao(desafioQuestao);

        System.out.println("Questão cadastrada com sucesso: " + questaoSalva.getId());


        // 7. Exibe uma mensagem e retorna para a mesma tela.
        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Questão cadastrada e adicionada ao desafio com sucesso!");

        return "redirect:/organizador/desafios/" + id + "/questoes";
    }
}