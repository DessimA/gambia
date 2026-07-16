import pytest
from app.core.classificador import ClassificadorEnergia
from app.models.schemas import ClassificacaoEficiencia, ClassificarRequest, TipoImovel


def test_classificar_retorna_categoria_e_probabilidade(classificador: ClassificadorEnergia, req_moderado: ClassificarRequest):
    categoria, prob = classificador.classificar(req_moderado)
    assert categoria in (ClassificacaoEficiencia.Eficiente, ClassificacaoEficiencia.Moderado, ClassificacaoEficiencia.Ineficiente)
    assert 0.0 <= prob <= 1.0


def test_classificar_ineficiente(classificador: ClassificadorEnergia, req_ineficiente: ClassificarRequest):
    categoria, prob = classificador.classificar(req_ineficiente)
    assert categoria == ClassificacaoEficiencia.Ineficiente
    assert prob >= 0.5


def test_classificar_eficiente(classificador: ClassificadorEnergia, req_eficiente: ClassificarRequest):
    categoria, prob = classificador.classificar(req_eficiente)
    assert categoria == ClassificacaoEficiencia.Eficiente
    assert prob >= 0.5


def test_obter_recomendacoes_eficiente(classificador: ClassificadorEnergia):
    recs = classificador.obter_recomendacoes(ClassificacaoEficiencia.Eficiente)
    assert len(recs) == 3
    assert all(isinstance(r, str) for r in recs)


def test_obter_recomendacoes_moderado(classificador: ClassificadorEnergia):
    recs = classificador.obter_recomendacoes(ClassificacaoEficiencia.Moderado)
    assert len(recs) == 3
    assert all(isinstance(r, str) for r in recs)


def test_obter_recomendacoes_ineficiente(classificador: ClassificadorEnergia):
    recs = classificador.obter_recomendacoes(ClassificacaoEficiencia.Ineficiente)
    assert len(recs) == 3
    assert all(isinstance(r, str) for r in recs)


def test_armazenar_recomendacoes(classificador: ClassificadorEnergia):
    novas = ["Recomendação A", "Recomendação B", "Recomendação C"]
    classificador.armazenar_recomendacoes(ClassificacaoEficiencia.Moderado, novas)
    recs = classificador.obter_recomendacoes(ClassificacaoEficiencia.Moderado)
    assert recs == novas


def test_armazenar_recomendacoes_vazias_nao_substitui(classificador: ClassificadorEnergia):
    originais = classificador.obter_recomendacoes(ClassificacaoEficiencia.Moderado)
    classificador.armazenar_recomendacoes(ClassificacaoEficiencia.Moderado, [])
    recs = classificador.obter_recomendacoes(ClassificacaoEficiencia.Moderado)
    assert recs == originais


def test_diferentes_tipos_imovel_nao_afetam_interface():
    classificador = ClassificadorEnergia()
    for tipo in TipoImovel:
        req = ClassificarRequest(
            consumo_kwh=200,
            uso_horario_pico=False,
            quantidade_equipamentos=8,
            tipo_imovel=tipo,
            horas_alto_consumo=4,
        )
        categoria, prob = classificador.classificar(req)
        assert isinstance(categoria, ClassificacaoEficiencia)
        assert isinstance(prob, float)
