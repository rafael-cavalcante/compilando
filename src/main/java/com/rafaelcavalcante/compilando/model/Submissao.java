package com.rafaelcavalcante.compilando.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.rafaelcavalcante.compilando.model.enums.StatusSubmissao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "submissoes")
public class Submissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "desafio_questao_id", nullable = false)
    private DesafioQuestao desafioQuestao;

    @Lob
    @Column(nullable = false)
    private String codigoFonte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusSubmissao status = StatusSubmissao.PENDENTE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pontuacao = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer totalCasos;

    @Column(nullable = false)
    private Integer casosAprovados = 0;

    private Long tempoExecucaoMs;

    private Long memoriaUtilizadaKb;

    @Column(columnDefinition = "TEXT")
    private String mensagemErro;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataEnvio;

    @PrePersist
    void prePersist() {
        this.dataEnvio = LocalDateTime.now();
    }
}
