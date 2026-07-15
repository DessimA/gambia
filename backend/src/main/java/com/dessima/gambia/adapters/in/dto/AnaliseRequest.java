package com.dessima.gambia.adapters.in.dto;

import com.dessima.gambia.domain.model.TipoImovel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record AnaliseRequest(
    UUID imovel_id,
    @NotNull @Positive Double consumo_kwh,
    @NotNull Boolean uso_horario_pico,
    @NotNull @Min(0) Integer quantidade_equipamentos,
    @NotNull TipoImovel tipo_imovel,
    @NotNull @Min(0) @Max(24) Integer horas_alto_consumo) {}
