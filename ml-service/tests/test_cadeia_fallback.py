import pytest

from app.core.classificador import ClassificadorEnergia
from app.core.cadeia_fallback import executar_cadeia
from app.core.llm_gerador import RecomendadorGroq
from app.models.schemas import ClassificacaoEficiencia, ClassificarRequest, TipoImovel


class AsyncMockLLM:
    def __init__(self, categoria=ClassificacaoEficiencia.Moderado, recomendacoes=None):
        self._categoria = categoria
        self._recomendacoes = recomendacoes or ["Rec 1", "Rec 2", "Rec 3"]

    async def classificar_e_recomendar(self, req):
        return self._categoria, self._recomendacoes

    async def gerar(self, req, categoria):
        return self._recomendacoes


@pytest.mark.asyncio
async def test_cadeia_com_ml_confianca_alta_usa_classificador():
    req = ClassificarRequest(
        consumo_kwh=500,
        uso_horario_pico=True,
        quantidade_equipamentos=15,
        tipo_imovel=TipoImovel.Industria,
        horas_alto_consumo=8,
    )
    classificador = ClassificadorEnergia()
    llm = AsyncMockLLM()

    resposta = await executar_cadeia(req, classificador, llm)

    assert resposta.categoria == ClassificacaoEficiencia.Ineficiente
    assert resposta.probabilidade >= 0.8
    assert len(resposta.recomendacoes) == 3


@pytest.mark.asyncio
async def test_cadeia_com_ml_confianca_baixa_chama_llm():
    req = ClassificarRequest(
        consumo_kwh=100,
        uso_horario_pico=False,
        quantidade_equipamentos=5,
        tipo_imovel=TipoImovel.Apartamento,
        horas_alto_consumo=2,
    )

    class ClassificadorBaixaConfianca(ClassificadorEnergia):
        def classificar(self, req):
            return ClassificacaoEficiencia.Moderado, 0.3

    classificador = ClassificadorBaixaConfianca()
    llm = AsyncMockLLM(categoria=ClassificacaoEficiencia.Eficiente, recomendacoes=["LLM Rec 1", "LLM Rec 2", "LLM Rec 3"])

    resposta = await executar_cadeia(req, classificador, llm)

    assert resposta.categoria == ClassificacaoEficiencia.Eficiente
    assert resposta.probabilidade == 0.85
    assert "LLM Rec 1" in resposta.recomendacoes


@pytest.mark.asyncio
async def test_cadeia_quando_ml_e_llm_falham_usa_heuristico():
    req = ClassificarRequest(
        consumo_kwh=100,
        uso_horario_pico=False,
        quantidade_equipamentos=5,
        tipo_imovel=TipoImovel.Apartamento,
        horas_alto_consumo=2,
    )

    class ClassificadorFalho(ClassificadorEnergia):
        def classificar(self, req):
            raise RuntimeError("ML failed")

    class LLMFalho:
        async def classificar_e_recomendar(self, req):
            raise RuntimeError("LLM failed")

    classificador = ClassificadorFalho()
    llm = LLMFalho()

    resposta = await executar_cadeia(req, classificador, llm)

    assert resposta.categoria == ClassificacaoEficiencia.Eficiente
    assert len(resposta.recomendacoes) == 3


@pytest.mark.asyncio
async def test_cadeia_llm_armazena_recomendacoes_no_classificador():
    req = ClassificarRequest(
        consumo_kwh=100,
        uso_horario_pico=False,
        quantidade_equipamentos=5,
        tipo_imovel=TipoImovel.Apartamento,
        horas_alto_consumo=2,
    )

    class ClassificadorComStorage(ClassificadorEnergia):
        def classificar(self, req):
            return ClassificacaoEficiencia.Moderado, 0.3

    classificador = ClassificadorComStorage()
    recs_llm = ["LLM Store 1", "LLM Store 2", "LLM Store 3"]
    llm = AsyncMockLLM(categoria=ClassificacaoEficiencia.Moderado, recomendacoes=recs_llm)

    await executar_cadeia(req, classificador, llm)

    stored = classificador.obter_recomendacoes(ClassificacaoEficiencia.Moderado)
    assert stored == recs_llm
