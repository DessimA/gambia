package com.dessima.gambia.adapters.in.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
    long totalAnalises,
    BigDecimal mediaConsumoKwh,
    BigDecimal totalCustoEstimado,
    BigDecimal totalEmissaoCo2Kg,
    List<ConsumoMensal> consumoPorMes) {

  public record ConsumoMensal(String mes, BigDecimal consumoKwh) {}
}
