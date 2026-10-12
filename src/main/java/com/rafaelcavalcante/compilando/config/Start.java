package com.rafaelcavalcante.compilando.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rafaelcavalcante.compilando.model.Organizador;
import com.rafaelcavalcante.compilando.model.Usuario;
import com.rafaelcavalcante.compilando.model.enums.PapelUsuario;
import com.rafaelcavalcante.compilando.repository.OrganizadorRepository;
import com.rafaelcavalcante.compilando.repository.UsuarioRepository;

@Component
public class Start implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
    private final OrganizadorRepository organizadorRepository;
    private final PasswordEncoder passwordEncoder;

    public Start(UsuarioRepository usuarioRepository, OrganizadorRepository organizadorRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.organizadorRepository = organizadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.organizadorRepository.count() > 0) {
            System.out.println(
                    "Organizador já cadastrado.");
            return;
        }

        String username = "admin";
        String password = "senha123";
        String email = "organizador@compilando.local";

        Usuario usuario = this.usuarioRepository
                .findByUsername(username)
                .orElseGet(() -> {
                    Usuario novo = new Usuario();
                    novo.setUsername(username);
                    novo.setPassword(
                            passwordEncoder.encode(password));
                    novo.setPapel(
                            PapelUsuario.ORGANIZADOR);
                    novo.setAtivo(true);

                    return usuarioRepository.save(novo);
                });

        if (usuario.getPapel() != PapelUsuario.ORGANIZADOR) {
            throw new IllegalStateException(
                    "O nome de usuário administrativo já pertence "
                            + "a um usuário com outro papel.");
        }

        Organizador organizador = new Organizador();
        organizador.setUsuario(usuario);
        organizador.setEmail(email);

        organizadorRepository.save(organizador);

        System.out.println(
                "Organizador inicial cadastrado com sucesso.");
    }

}
