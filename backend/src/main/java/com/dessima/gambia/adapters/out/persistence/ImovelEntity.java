package com.dessima.gambia.adapters.out.persistence;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_imovel")
public class ImovelEntity {

    @Id
    private UUID id;

    @Column(name = "tipo_imovel", nullable = false, length = 50)
    private String tipoImovel;

    @Column(name = "quantidade_equipamentos", nullable = false)
    private int quantidadeEquipamentos;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Deprecated
    protected ImovelEntity() {}

    public ImovelEntity(UUID id, String tipoImovel, int quantidadeEquipamentos) {
        this.id = id;
        this.tipoImovel = tipoImovel;
        this.quantidadeEquipamentos = quantidadeEquipamentos;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getTipoImovel() { return tipoImovel; }
    public int getQuantidadeEquipamentos() { return quantidadeEquipamentos; }
    public Instant getCreatedAt() { return createdAt; }
}
