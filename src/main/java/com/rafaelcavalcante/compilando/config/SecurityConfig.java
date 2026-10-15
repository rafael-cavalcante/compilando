package com.rafaelcavalcante.compilando.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                .requestMatchers("/organizador/**")
                    .hasRole("ORGANIZADOR")
                .requestMatchers("/equipe/**")
                    .hasRole("EQUIPE")
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/acesso-negado")
            )
            .formLogin(form -> form
                .successHandler(new CustomAuthenticationSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}