import { useState, type FormEvent } from 'react'
import type { AnaliseRequest, AnaliseResponse, TipoImovel } from './types'
import { analisarEnergia } from './services/api'
import './App.css'

const TIPOS_IMOVEL: TipoImovel[] = [
  'Casa', 'Apartamento', 'Comercio', 'Industria', 'Rural', 'Outro',
]

function App() {
  const [form, setForm] = useState<AnaliseRequest>({
    consumo_kwh: 0,
    uso_horario_pico: false,
    quantidade_equipamentos: 0,
    tipo_imovel: 'Casa',
    horas_alto_consumo: 0,
  })
  const [resultado, setResultado] = useState<AnaliseResponse | null>(null)
  const [erro, setErro] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setLoading(true)
    setErro(null)
    setResultado(null)
    try {
      const res = await analisarEnergia(form)
      setResultado(res)
    } catch (err) {
      setErro(err instanceof Error ? err.message : 'Erro desconhecido')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="app">
      <h1>GambIA</h1>
      <p className="subtitle">Análise de Eficiência Energética</p>

      <form onSubmit={handleSubmit} className="form">
        <label>
          Consumo (kWh)
          <input
            type="number"
            step="0.01"
            min="0"
            value={form.consumo_kwh}
            onChange={(e) => setForm({ ...form, consumo_kwh: +e.target.value })}
            required
          />
        </label>

        <label>
          Uso em horário de pico
          <input
            type="checkbox"
            checked={form.uso_horario_pico}
            onChange={(e) => setForm({ ...form, uso_horario_pico: e.target.checked })}
          />
        </label>

        <label>
          Quantidade de equipamentos
          <input
            type="number"
            min="0"
            value={form.quantidade_equipamentos}
            onChange={(e) => setForm({ ...form, quantidade_equipamentos: +e.target.value })}
            required
          />
        </label>

        <label>
          Tipo de imóvel
          <select
            value={form.tipo_imovel}
            onChange={(e) => setForm({ ...form, tipo_imovel: e.target.value as TipoImovel })}
          >
            {TIPOS_IMOVEL.map((t) => (
              <option key={t} value={t}>{t}</option>
            ))}
          </select>
        </label>

        <label>
          Horas de alto consumo/dia
          <input
            type="number"
            min="0"
            max="24"
            value={form.horas_alto_consumo}
            onChange={(e) => setForm({ ...form, horas_alto_consumo: +e.target.value })}
            required
          />
        </label>

        <button type="submit" disabled={loading}>
          {loading ? 'Analisando...' : 'Analisar'}
        </button>
      </form>

      {erro && <div className="error">{erro}</div>}

      {resultado && (
        <div className="resultado">
          <h2>Resultado</h2>
          <p><strong>Categoria:</strong> {resultado.categoria}</p>
          <p><strong>Confiança:</strong> {(resultado.probabilidade * 100).toFixed(1)}%</p>
          <p><strong>Custo estimado:</strong> R$ {resultado.custo_estimado_mensal.toFixed(2)}</p>
          <h3>Recomendações</h3>
          <ul>
            {resultado.recomendacoes.map((r, i) => (
              <li key={i}>{r}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  )
}

export default App
