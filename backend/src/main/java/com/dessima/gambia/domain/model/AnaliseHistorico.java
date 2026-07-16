package com.dessima.gambia.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AnaliseHistorico(
    UUID id,
    String categoria,
    BigDecimal probabilidade,
    BigDecimal consumoKwh,
    BigDecimal custoEstimadoMensal,
    BigDecimal emissaoCo2Kg,
    boolean usoHorarioPico,
    int horasAltoConsumo,
    Instant createdAt,
    List<String> recomendacoes) {}
