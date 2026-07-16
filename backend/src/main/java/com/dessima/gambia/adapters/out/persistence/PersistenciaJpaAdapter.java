package com.dessima.gambia.adapters.out.persistence;

import com.dessima.gambia.domain.model.AnaliseHistorico;
import com.dessima.gambia.domain.model.ResultadoAnalise;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;
import com.dessima.gambia.domain.ports.out.PersistenciaPort;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

  @Override
  public List<AnaliseHistorico> buscarAnalisesPorUsuario(UUID usuarioId) {
    var imoveis = imovelRepository.findByUsuarioId(usuarioId);
    if (imoveis.isEmpty()) return Collections.emptyList();

    var imovelIds = imoveis.stream().map(ImovelEntity::getId).toList();
    var analises = analiseRepository.findByImovelIdInOrderByCreatedAtDesc(imovelIds);

    return analises.stream().map(this::toHistorico).toList();
  }

  @Override
  public Optional<AnaliseHistorico> buscarAnalisePorId(UUID analiseId) {
    return analiseRepository.findById(analiseId).map(this::toHistorico);
  }

  private AnaliseHistorico toHistorico(AnaliseConsumoEntity entity) {
    var recomendacoes =
        recomendacaoRepository.findByAnaliseId(entity.getId()).stream()
            .map(RecomendacaoGeradaEntity::getRecomendacaoTexto)
            .toList();
    return new AnaliseHistorico(
        entity.getId(),
        entity.getCategoria(),
        entity.getProbabilidade(),
        entity.getConsumoKwh(),
        entity.getCustoEstimadoMensal(),
        entity.getEmissaoCo2Kg(),
        entity.isUsoHorarioPico(),
        entity.getHorasAltoConsumo(),
        entity.getCreatedAt(),
        recomendacoes);
  }
}
