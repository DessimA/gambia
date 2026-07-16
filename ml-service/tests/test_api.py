import pytest
from httpx import AsyncClient


@pytest.mark.asyncio
async def test_health_endpoint(client: AsyncClient):
    response = await client.get("/health")
    assert response.status_code == 200
    assert response.json() == {"status": "ok"}


@pytest.mark.asyncio
async def test_classificar_endpoint(client: AsyncClient):
    payload = {
        "consumo_kwh": 300,
        "uso_horario_pico": True,
        "quantidade_equipamentos": 10,
        "tipo_imovel": "Casa",
        "horas_alto_consumo": 5,
    }
    response = await client.post("/classificar", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "categoria" in data
    assert data["categoria"] in ("Eficiente", "Moderado", "Ineficiente")
    assert "probabilidade" in data
    assert 0.0 <= data["probabilidade"] <= 1.0
    assert "recomendacoes" in data
    assert len(data["recomendacoes"]) > 0


@pytest.mark.asyncio
async def test_classificar_ineficiente(client: AsyncClient):
    payload = {
        "consumo_kwh": 600,
        "uso_horario_pico": True,
        "quantidade_equipamentos": 20,
        "tipo_imovel": "Industria",
        "horas_alto_consumo": 10,
    }
    response = await client.post("/classificar", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["categoria"] == "Ineficiente"


@pytest.mark.asyncio
async def test_classificar_eficiente(client: AsyncClient):
    payload = {
        "consumo_kwh": 80,
        "uso_horario_pico": False,
        "quantidade_equipamentos": 3,
        "tipo_imovel": "Apartamento",
        "horas_alto_consumo": 1,
    }
    response = await client.post("/classificar", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["categoria"] in ("Eficiente", "Moderado")


@pytest.mark.asyncio
async def test_classificar_tipo_imovel_invalido(client: AsyncClient):
    payload = {
        "consumo_kwh": 300,
        "uso_horario_pico": True,
        "quantidade_equipamentos": 10,
        "tipo_imovel": "Invalido",
        "horas_alto_consumo": 5,
    }
    response = await client.post("/classificar", json=payload)
    assert response.status_code == 422


@pytest.mark.asyncio
async def test_classificar_consumo_negativo(client: AsyncClient):
    payload = {
        "consumo_kwh": -10,
        "uso_horario_pico": False,
        "quantidade_equipamentos": 5,
        "tipo_imovel": "Casa",
        "horas_alto_consumo": 2,
    }
    response = await client.post("/classificar", json=payload)
    assert response.status_code == 422


@pytest.mark.asyncio
async def test_classificar_horas_excedem_24(client: AsyncClient):
    payload = {
        "consumo_kwh": 300,
        "uso_horario_pico": False,
        "quantidade_equipamentos": 5,
        "tipo_imovel": "Casa",
        "horas_alto_consumo": 25,
    }
    response = await client.post("/classificar", json=payload)
    assert response.status_code == 422
