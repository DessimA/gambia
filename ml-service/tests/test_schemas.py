import pytest
from pydantic import ValidationError

from app.models.schemas import ClassificarRequest, ClassificarResponse, ClassificacaoEficiencia, TipoImovel


class TestClassificarRequest:
    def test_request_valido(self):
        req = ClassificarRequest(
            consumo_kwh=300.0,
            uso_horario_pico=True,
            quantidade_equipamentos=10,
            tipo_imovel=TipoImovel.Casa,
            horas_alto_consumo=5,
        )
        assert req.consumo_kwh == 300.0
        assert req.uso_horario_pico is True
        assert req.quantidade_equipamentos == 10
        assert req.tipo_imovel == TipoImovel.Casa
        assert req.horas_alto_consumo == 5

    def test_consumo_negativo_rejeitado(self):
        with pytest.raises(ValidationError):
            ClassificarRequest(
                consumo_kwh=-10,
                uso_horario_pico=False,
                quantidade_equipamentos=5,
                tipo_imovel=TipoImovel.Casa,
                horas_alto_consumo=2,
            )

    def test_consumo_zero_aceito_no_limite(self):
        with pytest.raises(ValidationError):
            ClassificarRequest(
                consumo_kwh=0,
                uso_horario_pico=False,
                quantidade_equipamentos=0,
                tipo_imovel=TipoImovel.Outro,
                horas_alto_consumo=0,
            )

    def test_horas_excedem_24_rejeitado(self):
        with pytest.raises(ValidationError):
            ClassificarRequest(
                consumo_kwh=200,
                uso_horario_pico=False,
                quantidade_equipamentos=5,
                tipo_imovel=TipoImovel.Casa,
                horas_alto_consumo=25,
            )

    def test_horas_negativas_rejeitado(self):
        with pytest.raises(ValidationError):
            ClassificarRequest(
                consumo_kwh=200,
                uso_horario_pico=False,
                quantidade_equipamentos=5,
                tipo_imovel=TipoImovel.Casa,
                horas_alto_consumo=-1,
            )

    def test_quantidade_equipamentos_negativa_rejeitado(self):
        with pytest.raises(ValidationError):
            ClassificarRequest(
                consumo_kwh=200,
                uso_horario_pico=False,
                quantidade_equipamentos=-1,
                tipo_imovel=TipoImovel.Casa,
                horas_alto_consumo=5,
            )


class TestClassificarResponse:
    def test_response_valido(self):
        resp = ClassificarResponse(
            categoria=ClassificacaoEficiencia.Moderado,
            probabilidade=0.95,
            recomendacoes=["Rec 1", "Rec 2", "Rec 3"],
        )
        assert resp.categoria == ClassificacaoEficiencia.Moderado
        assert resp.probabilidade == 0.95
        assert len(resp.recomendacoes) == 3

    def test_probabilidade_fora_do_range_rejeitado(self):
        with pytest.raises(ValidationError):
            ClassificarResponse(
                categoria=ClassificacaoEficiencia.Eficiente,
                probabilidade=1.5,
                recomendacoes=["Rec"],
            )

    def test_lista_recomendacoes_vazia_aceita(self):
        resp = ClassificarResponse(
            categoria=ClassificacaoEficiencia.Ineficiente,
            probabilidade=0.5,
            recomendacoes=[],
        )
        assert len(resp.recomendacoes) == 0


class TestEnums:
    def test_tipo_imovel_todos_os_valores(self):
        valores = [t.value for t in TipoImovel]
        assert "Casa" in valores
        assert "Apartamento" in valores
        assert "Comercio" in valores
        assert "Industria" in valores
        assert "Rural" in valores
        assert "Outro" in valores
        assert len(valores) == 6

    def test_classificacao_todos_os_valores(self):
        valores = [c.value for c in ClassificacaoEficiencia]
        assert "Eficiente" in valores
        assert "Moderado" in valores
        assert "Ineficiente" in valores
        assert len(valores) == 3
