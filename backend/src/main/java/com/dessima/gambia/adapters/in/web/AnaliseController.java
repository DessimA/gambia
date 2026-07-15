package com.dessima.gambia.adapters.in.web;

import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dessima.gambia.adapters.in.dto.AnaliseRequest;
import com.dessima.gambia.adapters.in.dto.AnaliseResponse;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;
import com.dessima.gambia.domain.model.TipoImovel;
import com.dessima.gambia.domain.ports.in.ObterAnaliseUseCase;

@RestController
@RequestMapping("/analise-energetica")
public class AnaliseController {

    private final ObterAnaliseUseCase useCase;

    public AnaliseController(ObterAnaliseUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<AnaliseResponse> analisar(@Valid @RequestBody AnaliseRequest request) {
        var solicitacao = new SolicitacaoAnalise(
                Optional.ofNullable(request.imovel_id()),
                request.consumo_kwh(),
                request.uso_horario_pico(),
                request.quantidade_equipamentos(),
                TipoImovel.valueOf(request.tipo_imovel()),
                request.horas_alto_consumo());

        var resultado = useCase.executar(solicitacao);
        return ResponseEntity.ok(AnaliseResponse.fromDomain(resultado));
    }
}
