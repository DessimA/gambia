from fastapi import APIRouter, Request

from app.core.cadeia_fallback import executar_cadeia
from app.models.schemas import ClassificarRequest, ClassificarResponse

router = APIRouter()


@router.post("/classificar", response_model=ClassificarResponse)
async def classificar_consumo(req: ClassificarRequest, request: Request):
    classificador = request.app.state.classificador
    llm = request.app.state.llm
    return await executar_cadeia(req, classificador, llm)
