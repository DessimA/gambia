from typing import AsyncGenerator

import pytest
from fastapi import FastAPI
from httpx import ASGITransport, AsyncClient

from app.core.classificador import ClassificadorEnergia
from app.core.llm_gerador import RecomendadorGroq
from app.models.schemas import ClassificarRequest, TipoImovel


@pytest.fixture
def classificador() -> ClassificadorEnergia:
    return ClassificadorEnergia()


@pytest.fixture
def llm() -> RecomendadorGroq:
    """LLM mock will be handled by monkeypatch in tests."""
    return RecomendadorGroq()


@pytest.fixture
def req_eficiente() -> ClassificarRequest:
    return ClassificarRequest(
        consumo_kwh=100,
        uso_horario_pico=False,
        quantidade_equipamentos=5,
        tipo_imovel=TipoImovel.Apartamento,
        horas_alto_consumo=2,
    )


@pytest.fixture
def req_moderado() -> ClassificarRequest:
    return ClassificarRequest(
        consumo_kwh=250,
        uso_horario_pico=False,
        quantidade_equipamentos=10,
        tipo_imovel=TipoImovel.Casa,
        horas_alto_consumo=5,
    )


@pytest.fixture
def req_ineficiente() -> ClassificarRequest:
    return ClassificarRequest(
        consumo_kwh=500,
        uso_horario_pico=True,
        quantidade_equipamentos=15,
        tipo_imovel=TipoImovel.Industria,
        horas_alto_consumo=8,
    )


@pytest.fixture
def app() -> FastAPI:
    from app.main import app
    return app


@pytest.fixture
async def client(app: FastAPI) -> AsyncGenerator[AsyncClient, None]:
    app.state.classificador = ClassificadorEnergia()
    app.state.llm = RecomendadorGroq()
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as ac:
        yield ac
