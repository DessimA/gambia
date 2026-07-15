package com.dessima.gambia.adapters.out.persistence;

import com.dessima.gambia.domain.model.ResultadoAnalise;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;
import com.dessima.gambia.domain.ports.out.PersistenciaPort;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PersistenciaJpaAdapter implements PersistenciaPort {

  private final ImovelRepository imovelRepository;
  private final AnaliseConsumoRepository analiseRepository;
  private final RecomendacaoGeradaRepository recomendacaoRepository;

  public PersistenciaJpaAdapter(
      ImovelRepository imovelRepository,
      AnaliseConsumoRepository analiseRepository,
      RecomendacaoGeradaRepository recomendacaoRepository) {
    this.imovelRepository = imovelRepository;
    this.analiseRepository = analiseRepository;
    this.recomendacaoRepository = recomendacaoRepository;
  }

  @Override
  public UUID salvarImovel(SolicitacaoAnalise solicitacao) {
    var id = solicitacao.imovelId().orElse(UUID.randomUUID());
    var entity =
        new ImovelEntity(
            id,
            solicitacao.tipoImovel().name(),
            solicitacao.quantidadeEquipamentos(),
            solicitacao.usuarioId().orElse(null));
    imovelRepository.save(entity);
    return id;
  }

  @Override
  public UUID salvarAnalise(
      UUID imovelId, SolicitacaoAnalise solicitacao, ResultadoAnalise resultado) {
    var id = UUID.randomUUID();
    var entity =
        new AnaliseConsumoEntity(
            id,
            imovelId,
            BigDecimal.valueOf(solicitacao.consumoKwh()),
            solicitacao.usoHorarioPico(),
            solicitacao.horasAltoConsumo(),
            resultado.categoria().name(),
            resultado.probabilidade(),
            resultado.custoEstimadoMensal(),
            resultado.emissaoCo2Kg());
    analiseRepository.save(entity);
    return id;
  }

  @Override
  public void salvarRecomendacoes(UUID analiseId, ResultadoAnalise resultado) {
    for (String texto : resultado.recomendacoes()) {
      var entity = new RecomendacaoGeradaEntity(UUID.randomUUID(), analiseId, texto);
      recomendacaoRepository.save(entity);
    }
  }
}
