package com.dessima.gambia.domain.model;

import java.math.BigDecimal;
import java.util.List;

public record ResultadoAnalise(
        ClassificacaoEficiencia categoria,
        BigDecimal probabilidade,
        List<String> recomendacoes,
        BigDecimal custoEstimadoMensal,
        BigDecimal emissaoCo2Kg
) {}
