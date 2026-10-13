package com.rafaelcavalcante.compilando.model;

import java.time.LocalDateTime;

import com.rafaelcavalcante.compilando.model.enums.Nivel;
import com.rafaelcavalcante.compilando.model.enums.StatusDesafio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "desafios")
public class Desafio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataTermino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Nivel nivel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private StatusDesafio status = StatusDesafio.RASCUNHO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizador_id", nullable = false)
    private Usuario organizador;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}