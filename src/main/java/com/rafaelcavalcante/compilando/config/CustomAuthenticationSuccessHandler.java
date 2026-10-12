package com.rafaelcavalcante.compilando.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

         boolean organizador = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority()
                        .equals("ROLE_ORGANIZADOR"));

        boolean equipe = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority()
                        .equals("ROLE_EQUIPE"));

        String destino;

        if (organizador) {
            destino = "/organizador";
        } else if (equipe) {
            destino = "/equipe";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.sendRedirect(request.getContextPath() + destino);
    }
    
}
