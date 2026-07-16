import pytest
from app.core.heuristica import classificar_heuristico
from app.models.schemas import ClassificacaoEficiencia, ClassificarRequest, TipoImovel


def test_alto_consumo_pico_muitas_horas_retorna_ineficiente():
    req = ClassificarRequest(
        consumo_kwh=500,
        uso_horario_pico=True,
        quantidade_equipamentos=15,
        tipo_imovel=TipoImovel.Industria,
        horas_alto_consumo=8,
    )
    assert classificar_heuristico(req) == ClassificacaoEficiencia.Ineficiente


def test_limiar_ineficiente():
    req = ClassificarRequest(
        consumo_kwh=401,
        uso_horario_pico=True,
        quantidade_equipamentos=10,
        tipo_imovel=TipoImovel.Casa,
        horas_alto_consumo=7,
    )
    assert classificar_heuristico(req) == ClassificacaoEficiencia.Ineficiente


def test_consumo_medio_retorna_moderado():
    req = ClassificarRequest(
        consumo_kwh=250,
        uso_horario_pico=False,
        quantidade_equipamentos=10,
        tipo_imovel=TipoImovel.Casa,
        horas_alto_consumo=5,
    )
    assert classificar_heuristico(req) == ClassificacaoEficiencia.Moderado


def test_consumo_alto_sem_pico_retorna_moderado():
    req = ClassificarRequest(
        consumo_kwh=201,
        uso_horario_pico=False,
        quantidade_equipamentos=10,
        tipo_imovel=TipoImovel.Casa,
        horas_alto_consumo=3,
    )
    assert classificar_heuristico(req) == ClassificacaoEficiencia.Moderado


def test_horas_alto_consumo_sem_pico_retorna_moderado():
    req = ClassificarRequest(
        consumo_kwh=100,
        uso_horario_pico=False,
        quantidade_equipamentos=10,
        tipo_imovel=TipoImovel.Casa,
        horas_alto_consumo=5,
    )
    assert classificar_heuristico(req) == ClassificacaoEficiencia.Moderado


def test_baixo_consumo_retorna_eficiente():
    req = ClassificarRequest(
        consumo_kwh=100,
        uso_horario_pico=False,
        quantidade_equipamentos=5,
        tipo_imovel=TipoImovel.Apartamento,
        horas_alto_consumo=2,
    )
    assert classificar_heuristico(req) == ClassificacaoEficiencia.Eficiente


def test_consumo_baixo_com_pico_mas_poucas_horas_retorna_eficiente():
    req = ClassificarRequest(
        consumo_kwh=50,
        uso_horario_pico=True,
        quantidade_equipamentos=3,
        tipo_imovel=TipoImovel.Casa,
        horas_alto_consumo=1,
    )
    assert classificar_heuristico(req) == ClassificacaoEficiencia.Eficiente


def test_valores_zero():
    req = ClassificarRequest(
        consumo_kwh=0.0,
        uso_horario_pico=False,
        quantidade_equipamentos=0,
        tipo_imovel=TipoImovel.Outro,
        horas_alto_consumo=0,
    )
    assert classificar_heuristico(req) == ClassificacaoEficiencia.Eficiente
