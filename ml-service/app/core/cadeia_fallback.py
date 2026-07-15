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

    # 2. Se ML tem confiança aceitável, usa resultado direto — sem LLM
    if probabilidade >= LIMIAR_CONFIANCA:
        logger.info("Confiança aceitável (%.2f) — pulando LLM", probabilidade)
        return ClassificarResponse(
            categoria=categoria,
            probabilidade=round(probabilidade, 4),
            recomendacoes=[],  # backend gera recomendações padrão
        )

    # 3. Confiança baixa — tenta LLM (Groq) como fallback para classificar + recomendar
    logger.info("Confiança baixa (%.2f) — tentando LLM fallback", probabilidade)
    try:
        categoria, recomendacoes = await llm.classificar_e_recomendar(req)
        probabilidade = 0.85
        logger.info("LLM fallback: %s", categoria.value)
    except Exception as e:
        logger.warning("LLM fallback failed: %s", e)
        categoria = classificar_heuristico(req)
        recomendacoes = [
            "Reduzir o uso de equipamentos durante horários de pico",
            "Avaliar aparelhos com alto consumo energético",
            "Distribuir atividades de maior consumo ao longo do dia",
        ]
        logger.info("Heuristic fallback: %s", categoria.value)

    return ClassificarResponse(
        categoria=categoria,
        probabilidade=round(probabilidade, 4),
        recomendacoes=recomendacoes,
    )
