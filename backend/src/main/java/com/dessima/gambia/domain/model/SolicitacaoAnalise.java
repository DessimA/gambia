package com.dessima.gambia.domain.model;

import java.util.Optional;
import java.util.UUID;

public record SolicitacaoAnalise(
    Optional<UUID> imovelId,
    double consumoKwh,
    boolean usoHorarioPico,
    int quantidadeEquipamentos,
    TipoImovel tipoImovel,
    int horasAltoConsumo) {}
