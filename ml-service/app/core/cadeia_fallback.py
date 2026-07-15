import logging
from typing import List

from app.models.schemas import ClassificarRequest, ClassificacaoEficiencia, ClassificarResponse
from app.core.classificador import ClassificadorEnergia
from app.core.llm_gerador import RecomendadorLLM
from app.core.heuristica import classificar_heuristico

logger = logging.getLogger(__name__)

LIMIAR_CONFIANCA = 0.80


async def executar_cadeia(
    req: ClassificarRequest,
    classificador: ClassificadorEnergia,
    llm: RecomendadorLLM,
) -> ClassificarResponse:
    categoria: ClassificacaoEficiencia
    probabilidade: float = 0.0
    recomendacoes: List[str] = []

    # 1. Classificador local
    try:
        categoria, probabilidade = classificador.classificar(req)
        logger.info("ML classifier: %s (%.2f)", categoria.value, probabilidade)
    except Exception as e:
        logger.warning("ML classifier failed: %s", e)
        categoria = ClassificacaoEficiencia.Moderado
        probabilidade = 0.0

    # 2. LLM para recomendações
    try:
        recomendacoes = await llm.gerar(req, categoria)
    except Exception as e:
        logger.warning("LLM failed: %s", e)
        recomendacoes = []

    # 3. Heurística como fallback se confiança baixa
    if probabilidade < LIMIAR_CONFIANCA:
        logger.info("Confiança baixa (%.2f), usando heurística", probabilidade)
        categoria = classificar_heuristico(req)
        if not recomendacoes:
            recomendacoes = [
                "Reduzir o uso de equipamentos durante horários de pico",
                "Avaliar aparelhos com alto consumo energético",
                "Distribuir atividades de maior consumo ao longo do dia",
            ]

    return ClassificarResponse(
        categoria=categoria,
        probabilidade=round(probabilidade, 4),
        recomendacoes=recomendacoes if recomendacoes else [
            "Reduzir o uso de equipamentos durante horários de pico",
            "Avaliar aparelhos com alto consumo energético",
            "Distribuir atividades de maior consumo ao longo do dia",
        ],
    )
