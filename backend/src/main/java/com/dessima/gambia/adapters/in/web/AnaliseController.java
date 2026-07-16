package com.dessima.gambia.adapters.in.web;

import com.dessima.gambia.adapters.in.dto.AnaliseRequest;
import com.dessima.gambia.adapters.in.dto.AnaliseResponse;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;
import com.dessima.gambia.domain.ports.in.ObterAnaliseUseCase;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analise-energetica")
public class AnaliseController {

  private final ObterAnaliseUseCase useCase;

  public AnaliseController(ObterAnaliseUseCase useCase) {
    this.useCase = useCase;
  }

  @PostMapping
  public ResponseEntity<AnaliseResponse> analisar(
      @Valid @RequestBody AnaliseRequest request, Authentication authentication) {
    Optional<UUID> usuarioId = Optional.empty();
    if (authentication != null
        && authentication.isAuthenticated()
        && !"dev-user".equals(authentication.getName())) {
      try {
        usuarioId = Optional.of(UUID.fromString(authentication.getName()));
      } catch (IllegalArgumentException e) {
      }
    }

    var solicitacao =
        new SolicitacaoAnalise(
            Optional.ofNullable(request.imovel_id()),
            request.consumo_kwh(),
            request.uso_horario_pico(),
            request.quantidade_equipamentos(),
            request.tipo_imovel(),
            request.horas_alto_consumo(),
            usuarioId);

    var resultado = useCase.executar(solicitacao);
    return ResponseEntity.ok(AnaliseResponse.fromDomain(resultado));
  }
}
