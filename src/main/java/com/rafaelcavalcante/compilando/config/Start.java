package com.rafaelcavalcante.compilando.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rafaelcavalcante.compilando.model.Usuario;
import com.rafaelcavalcante.compilando.model.enums.PapelUsuario;
import com.rafaelcavalcante.compilando.service.UsuarioService;

@Component
public class Start implements CommandLineRunner {
        private final UsuarioService usuarioService;
        private final PasswordEncoder passwordEncoder;

        public Start(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
                this.usuarioService = usuarioService;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        public void run(String... args) throws Exception {

                this.usuarioService
                                .pesquisarUsuarioPorUsername("admin")
                                .orElseGet(() -> {
                                        Usuario novo = new Usuario();
                                        novo.setUsername("admin");
                                        novo.setEmail("organizador@compilando.local");
                                        novo.setPassword(this.passwordEncoder.encode("senha123"));
                                        novo.setPapel(PapelUsuario.ORGANIZADOR);
                                        novo.setAtivo(true);

                                        return this.usuarioService.criarUsuario(novo);
                                });

                System.out.println("Administrador inicial verificado.");

                this.usuarioService
                                .pesquisarUsuarioPorUsername("equipe")
                                .orElseGet(() -> {
                                        Usuario novo = new Usuario();
                                        novo.setUsername("equipe");
                                        novo.setEmail("equipe@compilando.local");
                                        novo.setPassword(this.passwordEncoder.encode("senha123"));
                                        novo.setPapel(PapelUsuario.EQUIPE);
                                        novo.setAtivo(true);

                                        return this.usuarioService.criarUsuario(novo);
                                });

                System.out.println("Usuário de equipe inicial verificado.");
        }
}