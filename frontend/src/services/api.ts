import type { AnaliseRequest, AnaliseResponse } from '../types'

const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

function getCookie(name: string): string | null {
  const match = document.cookie.match(new RegExp(`(^| )${name}=([^;]+)`))
  return match ? decodeURIComponent(match[2]) : null
}

export async function analisarEnergia(
  data: AnaliseRequest
): Promise<AnaliseResponse> {
  const xsrfToken = getCookie('XSRF-TOKEN')
  const headers: Record<string, string> = { 'Content-Type': 'application/json' }
  if (xsrfToken) {
    headers['X-XSRF-TOKEN'] = xsrfToken
  }

  const response = await fetch(`${API_URL}/analise-energetica`, {
    method: 'POST',
    headers,
    credentials: 'include',
    body: JSON.stringify(data),
  })

  if (!response.ok) {
    const err = await response.json()
    throw new Error(err.mensagem ?? 'Erro ao analisar consumo')
  }

  return response.json()
}

export async function login(email: string, senha: string): Promise<void> {
  const response = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ email, senha }),
  })

  if (!response.ok) {
    const err = await response.json()
    throw new Error(err.mensagem ?? 'Erro ao fazer login')
  }
}

export async function cadastrar(nome: string, email: string, senha: string): Promise<void> {
  const response = await fetch(`${API_URL}/auth/cadastrar`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ nome, email, senha }),
  })

  if (!response.ok) {
    const err = await response.json()
    throw new Error(err.mensagem ?? 'Erro ao cadastrar')
  }
}
