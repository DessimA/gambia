package com.dessima.gambia.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_analise_consumo")
public class AnaliseConsumoEntity {

  @Id private UUID id;

  @Column(name = "imovel_id", nullable = false)
  private UUID imovelId;

  @Column(name = "consumo_kwh", nullable = false, precision = 10, scale = 2)
  private BigDecimal consumoKwh;

  @Column(name = "uso_horario_pico", nullable = false)
  private boolean usoHorarioPico;

  @Column(name = "horas_alto_consumo", nullable = false)
  private int horasAltoConsumo;

  @Column(name = "categoria", nullable = false, length = 30)
  private String categoria;

  @Column(name = "probabilidade", nullable = false, precision = 5, scale = 4)
  private BigDecimal probabilidade;

  @Column(name = "custo_estimado_mensal", nullable = false, precision = 10, scale = 2)
  private BigDecimal custoEstimadoMensal;

  @Column(name = "emissao_co2_kg", nullable = false, precision = 10, scale = 3)
  private BigDecimal emissaoCo2Kg;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Deprecated
  protected AnaliseConsumoEntity() {}

  public AnaliseConsumoEntity(
      UUID id,
      UUID imovelId,
      BigDecimal consumoKwh,
      boolean usoHorarioPico,
      int horasAltoConsumo,
      String categoria,
      BigDecimal probabilidade,
      BigDecimal custoEstimadoMensal,
      BigDecimal emissaoCo2Kg) {
    this.id = id;
    this.imovelId = imovelId;
    this.consumoKwh = consumoKwh;
    this.usoHorarioPico = usoHorarioPico;
    this.horasAltoConsumo = horasAltoConsumo;
    this.categoria = categoria;
    this.probabilidade = probabilidade;
    this.custoEstimadoMensal = custoEstimadoMensal;
    this.emissaoCo2Kg = emissaoCo2Kg;
    this.createdAt = Instant.now();
  }

  public UUID getId() {
    return id;
  }

  public UUID getImovelId() {
    return imovelId;
  }

  public BigDecimal getConsumoKwh() {
    return consumoKwh;
  }

  public boolean isUsoHorarioPico() {
    return usoHorarioPico;
  }

  public int getHorasAltoConsumo() {
    return horasAltoConsumo;
  }

  public String getCategoria() {
    return categoria;
  }

  public BigDecimal getProbabilidade() {
    return probabilidade;
  }

  public BigDecimal getCustoEstimadoMensal() {
    return custoEstimadoMensal;
  }

  public BigDecimal getEmissaoCo2Kg() {
    return emissaoCo2Kg;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
