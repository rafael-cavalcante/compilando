package com.rafaelcavalcante.compilando.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rafaelcavalcante.compilando.model.CasoTeste;
import com.rafaelcavalcante.compilando.model.Desafio;
import com.rafaelcavalcante.compilando.model.DesafioQuestao;
import com.rafaelcavalcante.compilando.model.Questao;
import com.rafaelcavalcante.compilando.model.Usuario;
import com.rafaelcavalcante.compilando.model.enums.Nivel;
import com.rafaelcavalcante.compilando.model.enums.PapelUsuario;
import com.rafaelcavalcante.compilando.model.enums.StatusDesafio;
import com.rafaelcavalcante.compilando.service.CasoTesteService;
import com.rafaelcavalcante.compilando.service.DesafioQuestaoService;
import com.rafaelcavalcante.compilando.service.DesafioService;
import com.rafaelcavalcante.compilando.service.QuestaoService;
import com.rafaelcavalcante.compilando.service.UsuarioService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/organizador")
public class OrganizadorController {
    private final UsuarioService usuarioService;
    private final DesafioService desafioService;
    private final QuestaoService questaoService;
    private final DesafioQuestaoService desafioQuestaoService;
    private final CasoTesteService casoTesteService;

    public OrganizadorController(UsuarioService usuarioService, DesafioService desafioService,
            QuestaoService questaoService, DesafioQuestaoService desafioQuestaoService,
            CasoTesteService casoTesteService) {
        this.usuarioService = usuarioService;
        this.desafioService = desafioService;
        this.questaoService = questaoService;
        this.desafioQuestaoService = desafioQuestaoService;
        this.casoTesteService = casoTesteService;
    }

    // Inicial
    @GetMapping()
    public String home(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("organizador", usuario.getUsername());
        model.addAttribute("desafios", this.desafioService.listarDesafios(usuario.getId()));
        model.addAttribute("totalDesafios", this.desafioService.totalDesafios());
        model.addAttribute("totalPublicados", this.desafioService.totalDesafiosPublicados());
        model.addAttribute("totalEquipes", this.usuarioService.contarUsuariosPorPapel(PapelUsuario.EQUIPE));

        return "organizador/home";
    }

    // Listar Desafios
    @GetMapping("/desafios")
    public String desafios(@AuthenticationPrincipal Usuario usuario, @RequestParam(defaultValue = "0") int page,
            Model model) {
        model.addAttribute("organizador", usuario.getUsername());
        model.addAttribute("paginaDesafios", this.desafioService.listarDesafios(usuario.getId(), page, 9));

        return "organizador/desafios";
    }

    // Criar Desafio GET
    @GetMapping("/desafios/novo")
    public String novoDesafio(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("desafio", new Desafio());
        model.addAttribute("niveis", Nivel.values());
        model.addAttribute("statusDesafio", StatusDesafio.values());
        model.addAttribute("nomeOrganizador", usuario.getUsername());
        return "organizador/desafioForm";
    }

    // Criar Desafio POST
    @PostMapping("/desafios/novo")
    public String cadastrar(@AuthenticationPrincipal Usuario usuario, @Valid Desafio desafio) {
        Usuario organizador = this.usuarioService
                .pesquisarUsuarioPorUsername(usuario.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                        "Organizador não encontrado para o usuário autenticado."));

        desafio.setOrganizador(organizador);

        Desafio desafioSalvo = this.desafioService.criarDesafio(desafio);

        return "redirect:/organizador/desafios/" + desafioSalvo.getId() + "/questoes";
    }

    // Editar Desafio
    @GetMapping("/desafios/{desafioId}/editar")
    public String editarDesafio(@AuthenticationPrincipal Usuario usuario, @PathVariable Long desafioId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Desafio desafio = this.desafioService.buscarDesafio(desafioId, usuario.getId());

            model.addAttribute("desafio", desafio);
            model.addAttribute("niveis", Nivel.values());
            model.addAttribute("statusDesafio", StatusDesafio.values());
            model.addAttribute("nomeOrganizador", usuario.getUsername());

            return "organizador/desafioForm";

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Desafio não encontrado.");

        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Acesso restrito! Você não é o proprietário deste desafio.");
        }
        return "redirect:/organizador/desafios";
    }

    // Apagar Desafio
    @PostMapping("/desafios/{desafioId}/apagar")
    public String apagarDesafio(@AuthenticationPrincipal Usuario usuario, @PathVariable Long desafioId,
            RedirectAttributes redirectAttributes) {
        try {
            this.desafioService.excluirDesafio(desafioId, usuario.getId());

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Desafio excluído com sucesso.");

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Desafio não encontrado.");

        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Acesso restrito! Você não é o proprietário deste desafio.");
        }

        return "redirect:/organizador/desafios";
    }

    // Método para exibir a lista de questões de um desafio
    @GetMapping("/questoes")
    public String questoes(@AuthenticationPrincipal Usuario usuario, @RequestParam(defaultValue = "0") int page,
            Model model) {
        model.addAttribute("organizador", usuario.getUsername());
        model.addAttribute("paginaQuestoes", this.questaoService.listarQuestoes(page, 9));

        return "organizador/questoes";
    }

    @GetMapping("/desafios/{desafioId}/questoes")
    public String novaQuestao(@AuthenticationPrincipal Usuario usuario, @PathVariable Long desafioId, Model model) {
        Desafio desafio = this.desafioService.buscarDesafioPorId(desafioId);
        List<Questao> questoes = this.questaoService.listarQuestoesPorDesafioId(desafio.getId());

        model.addAttribute("questao", new Questao());
        model.addAttribute("niveis", Nivel.values());
        model.addAttribute("desafio", desafio);
        model.addAttribute("questoes", questoes);

        return "organizador/questaoForm";

    }

    @PostMapping("/desafios/{desafioId}/questoes/nova")
    public String adicionarQuestao(@AuthenticationPrincipal Usuario usuario, @PathVariable Long desafioId,
            @Valid Questao questao, Model model, RedirectAttributes redirectAttributes) {

        Usuario autor = this.usuarioService
                .pesquisarUsuarioPorUsername(usuario.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Organizador não encontrado!"));

        Desafio desafio = this.desafioService.buscarDesafioPorId(desafioId);

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

        Questao questaoSalva = this.questaoService.adicionarQuestao(novaQuestao);

        DesafioQuestao desafioQuestao = new DesafioQuestao();
        desafioQuestao.setDesafio(desafio);
        desafioQuestao.setQuestao(questaoSalva);

        Integer ultimaOrdem = desafioQuestaoService.buscarMaiorOrdemPorDesafio(desafio.getId()).getOrdem();

        desafioQuestao.setOrdem(ultimaOrdem == null ? 1 : ultimaOrdem + 1);

        this.desafioQuestaoService.salvarDesafioQuestao(desafioQuestao);

        System.out.println("Questão cadastrada com sucesso: " + questaoSalva.getId());

        // 7. Exibe uma mensagem e retorna para a mesma tela.
        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Questão cadastrada e adicionada ao desafio com sucesso!");

        return "redirect:/organizador/desafios/" + desafioId + "/questoes";
    }

    // Rota de editar Questão
    @GetMapping("/desafios/{desafioId}/questoes/{questaoId}/editar")
    public String editarQuestao(@AuthenticationPrincipal Usuario usuario, @PathVariable Long desafioId, @PathVariable Long questaoId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Desafio desafio = this.desafioService.buscarDesafioPorId(desafioId);
            List<Questao> questoes = this.questaoService.listarQuestoesPorDesafioId(desafio.getId());
            Questao questao = this.questaoService.buscarQuestao(questaoId, usuario.getId());

            model.addAttribute("questao", questao);
            model.addAttribute("niveis", Nivel.values());
            model.addAttribute("desafio", desafio);
            model.addAttribute("questoes", questoes);

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Questão editada com sucesso.");

            return "organizador/questaoForm";

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Questão não encontrado.");

        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Acesso restrito! Você não é o proprietário desta questão.");
        }
        return "redirect:/organizador/questoes";
    }

    // Rota de apagar Questão
    @PostMapping("/questoes/{questaoId}/apagar")
    public String apagarQuestao(@AuthenticationPrincipal Usuario usuario, @PathVariable Long questaoId,
            RedirectAttributes redirectAttributes) {
        try {
            this.questaoService.excluirQuestao(questaoId, usuario.getId());

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Questão excluído com sucesso.");

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Questão não encontrado.");

        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Acesso restrito! Você não é o proprietário desta questão.");
        }

        return "redirect:/organizador/questoes";
    }

    // Método para exibir o formulário de adição de caso de teste
    @GetMapping("/questoes/{questaoId}/casoTeste")
    public String adicionarCasoTeste(@PathVariable Long questaoId, Model model) {
        Questao questao = this.questaoService.buscarQuestaoPorId(questaoId);

        model.addAttribute("casoTeste", new CasoTeste());
        model.addAttribute("questao", questao);
        model.addAttribute("casosTeste", this.casoTesteService.listarCasosTeste(questaoId));
        return "organizador/casoTesteForm";
    }

    @PostMapping("/questoes/{questaoId}/casoTeste/novo")
    public String salvarCasoTeste(@PathVariable Long questaoId, @Valid CasoTeste casoTeste, Model model,
            RedirectAttributes redirectAttributes) {
        Questao questao = this.questaoService.buscarQuestaoPorId(questaoId);

        casoTeste.setQuestao(questao);
        this.casoTesteService.adicionarCasoTeste(casoTeste);

        redirectAttributes.addFlashAttribute("mensagem", "Caso de teste adicionado com sucesso!");
        return "redirect:/organizador/questoes/" + questaoId + "/casoTeste";
    }

    @GetMapping("/casosTeste")
    public String casosTeste(@AuthenticationPrincipal Usuario usuario, @RequestParam(defaultValue = "0") int page,
            Model model) {
        model.addAttribute("organizador", usuario.getUsername());
        model.addAttribute("paginaCasosTeste", this.casoTesteService.listarCasosTeste(page, 9));

        return "organizador/casosTeste";
    }

    @GetMapping("questoes/{questaoId}/casosTeste")
    public String listarCasosTeste(@AuthenticationPrincipal Usuario usuario, @PathVariable Long questaoId,
            Model model) {
        model.addAttribute("organizador", usuario.getUsername());
        model.addAttribute("questao", this.questaoService.buscarQuestaoPorId(questaoId));
        model.addAttribute("casosTeste", this.casoTesteService.listarCasosTeste(questaoId));

        return "organizador/casosTeste";
    }
}