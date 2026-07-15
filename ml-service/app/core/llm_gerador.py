import os
import logging
from typing import List

from app.models.schemas import ClassificarRequest, ClassificacaoEficiencia

logger = logging.getLogger(__name__)


class RecomendadorLLM:
    """Gera recomendações usando Qwen2.5-1.5B-Instruct via llama.cpp ou similar.

    Nota: O carregamento real do modelo requer ~3 GB RAM (FP16).
    Esta implementação usa fallback para respostas padrão caso o
    modelo não esteja disponível.
    """

    def __init__(self, model_path: str | None = None):
        self._model_path = model_path
        self._model = None
        self._disponivel = False

    async def carregar(self):
        """Tenta carregar o modelo Qwen. Se falhar, opera sem ele."""
        try:
            if self._model_path and os.path.exists(self._model_path):
                logger.info("LLM model found at %s", self._model_path)
                self._disponivel = True
            else:
                logger.warning("LLM model not found, using fallback")
        except Exception as e:
            logger.warning("Failed to load LLM: %s", e)

    async def gerar(
        self,
        req: ClassificarRequest,
        categoria: ClassificacaoEficiencia,
    ) -> List[str]:
        if self._disponivel:
            try:
                return await self._inferir(req, categoria)
            except Exception as e:
                logger.error("LLM inference failed: %s", e)
        return self._recomendacoes_padrao(req, categoria)

    async def _inferir(
        self,
        req: ClassificarRequest,
        categoria: ClassificacaoEficiencia,
    ) -> List[str]:
        raise NotImplementedError("Real LLM inference requires Qwen model deployment")

    @staticmethod
    def _recomendacoes_padrao(
        req: ClassificarRequest,
        categoria: ClassificacaoEficiencia,
    ) -> List[str]:
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
