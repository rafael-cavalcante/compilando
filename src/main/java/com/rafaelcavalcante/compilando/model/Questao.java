package com.rafaelcavalcante.compilando.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.rafaelcavalcante.compilando.model.enums.Nivel;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "questoes")
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String formatoEntrada;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String formatoSaida;

    @Column(columnDefinition = "TEXT")
    private String restricoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Nivel nivel;

    @Column(nullable = false)
    private Integer tempoLimiteMs;

    @Column(nullable = false)
    private Integer memoriaLimiteMb;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @OneToMany(
        mappedBy = "questao",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<DesafioQuestao> desafios = new ArrayList<>();

    @OneToMany(
        mappedBy = "questao",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<CasoTeste> casosTeste = new ArrayList<>();

    @PrePersist
    void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}