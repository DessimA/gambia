package com.dessima.gambia.adapters.in.web;

import com.dessima.gambia.adapters.in.dto.AnaliseHistoricoResponse;
import com.dessima.gambia.adapters.in.dto.DashboardResponse;
import com.dessima.gambia.adapters.in.dto.DashboardResponse.ConsumoMensal;
import com.dessima.gambia.domain.model.AnaliseHistorico;
import com.dessima.gambia.domain.ports.in.HistoricoUseCase;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoricoController {

  private final HistoricoUseCase historicoUseCase;

  public HistoricoController(HistoricoUseCase historicoUseCase) {
    this.historicoUseCase = historicoUseCase;
  }

  @GetMapping("/analises")
  public ResponseEntity<List<AnaliseHistoricoResponse>> listarAnalises(Authentication auth) {
    if (auth == null) {
      return ResponseEntity.ok(List.of());
    }
    var usuarioId = UUID.fromString(auth.getName());
    var analises = historicoUseCase.listarAnalises(usuarioId);
    var response = analises.stream().map(AnaliseHistoricoResponse::fromDomain).toList();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/analises/{id}")
  public ResponseEntity<AnaliseHistoricoResponse> buscarAnalise(@PathVariable UUID id) {
    return historicoUseCase
        .buscarAnalise(id)
        .map(a -> ResponseEntity.ok(AnaliseHistoricoResponse.fromDomain(a)))
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/dashboard")
  public ResponseEntity<DashboardResponse> dashboard(Authentication auth) {
    if (auth == null) {
      return ResponseEntity.ok(
          new DashboardResponse(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, List.of()));
    }
    var usuarioId = UUID.fromString(auth.getName());
    var analises = historicoUseCase.listarAnalises(usuarioId);

    if (analises.isEmpty()) {
      return ResponseEntity.ok(
          new DashboardResponse(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, List.of()));
    }

    long total = analises.size();
    var somaConsumo =
        analises.stream()
            .map(AnaliseHistorico::consumoKwh)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    var media = somaConsumo.divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    var somaCusto =
        analises.stream()
            .map(AnaliseHistorico::custoEstimadoMensal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    var somaEmissao =
        analises.stream()
            .map(AnaliseHistorico::emissaoCo2Kg)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    var consumoPorMes =
        analises.stream()
            .collect(
                Collectors.groupingBy(
                    a ->
                        a.createdAt().atZone(ZoneId.systemDefault()).getYear()
                            + "-"
                            + String.format(
                                "%02d",
                                a.createdAt().atZone(ZoneId.systemDefault()).getMonthValue()),
                    LinkedHashMap::new,
                    Collectors.reducing(
                        BigDecimal.ZERO, AnaliseHistorico::consumoKwh, BigDecimal::add)))
            .entrySet()
            .stream()
            .map(e -> new ConsumoMensal(e.getKey(), e.getValue()))
            .toList();

    return ResponseEntity.ok(
        new DashboardResponse(total, media, somaCusto, somaEmissao, consumoPorMes));
  }
}
