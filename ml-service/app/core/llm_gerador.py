import os
import logging
from typing import List

from groq import AsyncGroq

from app.models.schemas import ClassificarRequest, ClassificacaoEficiencia

logger = logging.getLogger(__name__)

SYSTEM_PROMPT = (
    "Você é um assistente especialista em sustentabilidade e eficiência "
    "energética residencial. Com base nos dados que eu fornecer, gere "
    "exatamente 3 recomendações de economia de energia curtas, práticas e "
    "diretas em português. Retorne estritamente um item por linha, sem "
    "numeração, sem marcadores e sem textos explicativos adicionais antes "
    "ou depois."
)

MODELO_PADRAO = "llama-3.3-70b-versatile"


class RecomendadorGroq:
    """Gera recomendações via Groq API (OpenAI-compatible)."""

    def __init__(self):
        api_key = os.environ.get("GROQ_API_KEY", "")
        self._modelo = os.environ.get("GROQ_MODEL_ID", MODELO_PADRAO)
        self._disponivel = bool(api_key)
        if self._disponivel:
            self._client = AsyncGroq(api_key=api_key)
        else:
            self._client = None
            logger.warning("GROQ_API_KEY not set — LLM will use static fallback")

    async def gerar(
        self,
        req: ClassificarRequest,
        categoria: ClassificacaoEficiencia,
    ) -> List[str]:
        if self._disponivel:
            try:
                return await self._inferir(req, categoria)
            except Exception as e:
                logger.error("Groq API call failed: %s", e)
        return self._recomendacoes_padrao(categoria)

    async def _inferir(
        self,
        req: ClassificarRequest,
        categoria: ClassificacaoEficiencia,
    ) -> List[str]:
        user_prompt = (
            f"Dados do imóvel: consumo: {req.consumo_kwh:.0f}kWh, "
            f"categoria de eficiência do modelo: {categoria.value}, "
            f"uso em horário de pico: {'Sim' if req.uso_horario_pico else 'Nao'}, "
            f"horas de alto consumo: {req.horas_alto_consumo}h por dia."
        )

        response = await self._client.chat.completions.create(
            model=self._modelo,
            messages=[
                {"role": "system", "content": SYSTEM_PROMPT},
                {"role": "user", "content": user_prompt},
            ],
            temperature=0.3,
            max_tokens=300,
        )

        content = response.choices[0].message.content or ""
        linhas = [
            linha.strip() for linha in content.split("\n")
            if linha.strip() and not linha.strip().startswith(("-", "1", "2", "3"))
        ]
        return linhas[:3] if len(linhas) >= 3 else linhas + self._recomendacoes_padrao(categoria)[: 3 - len(linhas)]

    @staticmethod
    def _recomendacoes_padrao(categoria: ClassificacaoEficiencia) -> List[str]:
        base = [
            "Reduzir o uso de equipamentos durante horários de pico",
            "Avaliar aparelhos com alto consumo energético",
            "Distribuir atividades de maior consumo ao longo do dia",
        ]
        if categoria == ClassificacaoEficiencia.Ineficiente:
            return [
                "Substituir lâmpadas incandescentes por LED",
                "Realizar manutenção periódica em equipamentos de alto consumo",
                "Investir em sistemas de energia solar fotovoltaica",
            ]
        if categoria == ClassificacaoEficiencia.Moderado:
            return base
        return [
            "Manter os hábitos atuais de consumo consciente",
            "Acompanhar mensalmente a fatura de energia",
            "Compartilhar práticas sustentáveis com a comunidade",
        ]
