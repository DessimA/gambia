import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import StandardScaler

from app.models.schemas import ClassificarRequest, ClassificacaoEficiencia

LABEL_MAP = ["Eficiente", "Moderado", "Ineficiente"]
TIPO_IMOVEL_MAP = {
    "Casa": 0, "Apartamento": 1, "Comercio": 2,
    "Industria": 3, "Rural": 4, "Outro": 5,
}


class ClassificadorEnergia:
    def __init__(self):
        self._scaler = StandardScaler()
        self._model = self._gerar_modelo_sintetico()

    def _gerar_modelo_sintetico(self) -> RandomForestClassifier:
        """Gera dados sintéticos de treino com regras limiares (README §3.1.B)."""
        rng = np.random.default_rng(42)
        n = 500

        consumo = rng.uniform(50, 800, n)
        pico = rng.integers(0, 2, n)
        equip = rng.integers(1, 20, n)
        tipo = rng.integers(0, 6, n)
        horas = rng.uniform(0, 12, n)

        labels = []
        for i in range(n):
            if consumo[i] > 400 and horas[i] > 6 and pico[i] == 1:
                labels.append(2)
            elif consumo[i] > 200 or horas[i] > 4:
                labels.append(1)
            else:
                labels.append(0)

        X = np.column_stack((consumo, pico, equip, tipo, horas))
        self._scaler.fit(X)
        X_scaled = self._scaler.transform(X)

        model = RandomForestClassifier(
            n_estimators=100, max_depth=10, random_state=42
        )
        model.fit(X_scaled, labels)
        return model

    def _extrair_features(self, req: ClassificarRequest) -> np.ndarray:
        tipo_idx = TIPO_IMOVEL_MAP.get(req.tipo_imovel.value, 5)
        return np.array([[
            req.consumo_kwh,
            int(req.uso_horario_pico),
            req.quantidade_equipamentos,
            tipo_idx,
            req.horas_alto_consumo,
        ]])

    def classificar(self, req: ClassificarRequest) -> tuple[ClassificacaoEficiencia, float]:
        X = self._extrair_features(req)
        X_scaled = self._scaler.transform(X)
        pred = int(self._model.predict(X_scaled)[0])
        probas = self._model.predict_proba(X_scaled)[0]
        return ClassificacaoEficiencia(LABEL_MAP[pred]), float(max(probas))
