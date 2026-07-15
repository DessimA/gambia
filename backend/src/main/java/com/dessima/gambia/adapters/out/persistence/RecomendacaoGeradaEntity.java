package com.dessima.gambia.adapters.out.persistence;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_recomendacao_gerada")
public class RecomendacaoGeradaEntity {

    @Id
    private UUID id;

    @Column(name = "analise_id", nullable = false)
    private UUID analiseId;

    @Column(name = "recomendacao_texto", nullable = false, columnDefinition = "TEXT")
    private String recomendacaoTexto;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Deprecated
    protected RecomendacaoGeradaEntity() {}

    public RecomendacaoGeradaEntity(UUID id, UUID analiseId, String recomendacaoTexto) {
        this.id = id;
        this.analiseId = analiseId;
        this.recomendacaoTexto = recomendacaoTexto;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getAnaliseId() { return analiseId; }
    public String getRecomendacaoTexto() { return recomendacaoTexto; }
    public Instant getCreatedAt() { return createdAt; }
}
