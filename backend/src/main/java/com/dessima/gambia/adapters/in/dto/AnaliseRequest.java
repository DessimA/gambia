package com.dessima.gambia.adapters.in.dto;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AnaliseRequest(
        UUID imovel_id,

        @NotNull @Positive
        Double consumo_kwh,

        @NotNull
        Boolean uso_horario_pico,

        @NotNull @Min(0)
        Integer quantidade_equipamentos,

        @NotBlank
        String tipo_imovel,

        @NotNull @Min(0) @Max(24)
        Integer horas_alto_consumo
) {}
