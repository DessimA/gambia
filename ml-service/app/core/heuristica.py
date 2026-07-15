from app.models.schemas import ClassificarRequest, ClassificacaoEficiencia


def classificar_heuristico(req: ClassificarRequest) -> ClassificacaoEficiencia:
    """Última barreira de fallback: regras de negócio estáticas."""
    if req.consumo_kwh > 400 and req.horas_alto_consumo > 6 and req.uso_horario_pico:
        return ClassificacaoEficiencia.Ineficiente
    if req.consumo_kwh > 200 or req.horas_alto_consumo > 4:
        return ClassificacaoEficiencia.Moderado
    return ClassificacaoEficiencia.Eficiente
