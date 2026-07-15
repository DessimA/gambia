import type { AnaliseRequest, AnaliseResponse } from '../types'

const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

export async function analisarEnergia(
  data: AnaliseRequest
): Promise<AnaliseResponse> {
  const response = await fetch(`${API_URL}/analise-energetica`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(data),
  })

  if (!response.ok) {
    const err = await response.json()
    throw new Error(err.mensagem ?? 'Erro ao analisar consumo')
  }

  return response.json()
}
