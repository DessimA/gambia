from enum import Enum
from typing import List

from pydantic import BaseModel, Field


class TipoImovel(str, Enum):
    Casa = "Casa"
    Apartamento = "Apartamento"
    Comercio = "Comercio"
    Industria = "Industria"
    Rural = "Rural"
    Outro = "Outro"


class ClassificacaoEficiencia(str, Enum):
    Eficiente = "Eficiente"
    Moderado = "Moderado"
    Ineficiente = "Ineficiente"


class ClassificarRequest(BaseModel):
    consumo_kwh: float = Field(..., gt=0)
    uso_horario_pico: bool
    quantidade_equipamentos: int = Field(..., ge=0)
    tipo_imovel: TipoImovel
    horas_alto_consumo: int = Field(..., ge=0, le=24)


class ClassificarResponse(BaseModel):
    categoria: ClassificacaoEficiencia
    probabilidade: float
    recomendacoes: List[str]
