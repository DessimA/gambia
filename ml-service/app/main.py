import logging
from contextlib import asynccontextmanager

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.api.classificar import router as classificar_router
from app.core.classificador import ClassificadorEnergia
from app.core.llm_gerador import RecomendadorGroq

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


@asynccontextmanager
async def lifespan(app: FastAPI):
    logger.info("Carregando modelo classificador...")
    classificador = ClassificadorEnergia()
    app.state.classificador = classificador

    logger.info("Inicializando Groq LLM...")
    llm = RecomendadorGroq()
    app.state.llm = llm

    logger.info("ML Service ready")
    yield
    logger.info("ML Service shutting down")


app = FastAPI(
    title="GambIA ML Service",
    version="0.1.0",
    lifespan=lifespan,
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(classificar_router)


@app.get("/health")
async def health():
    return {"status": "ok"}
