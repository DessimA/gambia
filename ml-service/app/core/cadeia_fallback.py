import logging
from typing import List

from app.core.classificador import ClassificadorEnergia
from app.core.heuristica import classificar_heuristico
from app.core.llm_gerador import RecomendadorGroq
from app.models.schemas import ClassificacaoEficiencia, ClassificarRequest, ClassificarResponse

logger = logging.getLogger(__name__)

LIMIAR_CONFIANCA = 0.80


async def executar_cadeia(
    req: ClassificarRequest,
    classificador: ClassificadorEnergia,
    llm: RecomendadorGroq,
) -> ClassificarResponse:
    categoria: ClassificacaoEficiencia
    probabilidade: float = 0.0
    recomendacoes: List[str] = []

    # 1. Classificador local (ML) — via principal
    try:
        categoria, probabilidade = classificador.classificar(req)
        logger.info("ML classifier: %s (%.2f)", categoria.value, probabilidade)
    except Exception as e:
        logger.warning("ML classifier failed: %s", e)
        probabilidade = 0.0

    # 2. Se ML tem confiança aceitável, usa resultado direto com recomendações do treinamento
    if probabilidade >= LIMIAR_CONFIANCA:
        recomendacoes = classificador.obter_recomendacoes(categoria)
        logger.info(
            "Confiança aceitável (%.2f) — recomendações do treinamento: %s",
            probabilidade, recomendacoes,
        )
        return ClassificarResponse(
            categoria=categoria,
            probabilidade=round(probabilidade, 4),
            recomendacoes=recomendacoes,
        )

    # 3. Confiança baixa — tenta LLM (Groq) como fallback para classificar + recomendar
    logger.info("Confiança baixa (%.2f) — tentando LLM fallback", probabilidade)
    try:
        categoria, recomendacoes = await llm.classificar_e_recomendar(req)
        probabilidade = 0.85
        classificador.armazenar_recomendacoes(categoria, recomendacoes)
        logger.info("LLM fallback: %s — recomendações armazenadas no treinamento", categoria.value)
    except Exception as e:
        logger.warning("LLM fallback failed: %s", e)
        categoria = classificar_heuristico(req)
        recomendacoes = classificador.obter_recomendacoes(categoria)
        logger.info("Heuristic fallback: %s", categoria.value)

    return ClassificarResponse(
        categoria=categoria,
        probabilidade=round(probabilidade, 4),
        recomendacoes=recomendacoes,
    )
