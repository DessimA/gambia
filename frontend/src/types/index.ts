export interface Usuario {
  id: string
  nome: string
  email: string
}

export interface AnaliseRequest {
  imovel_id?: string
  consumo_kwh: number
  uso_horario_pico: boolean
  quantidade_equipamentos: number
  tipo_imovel: TipoImovel
  horas_alto_consumo: number
}

export interface AnaliseResponse {
  categoria: ClassificacaoEficiencia
  probabilidade: number
  recomendacoes: string[]
  custo_estimado_mensal: number
}

export type ClassificacaoEficiencia = 'Eficiente' | 'Moderado' | 'Ineficiente'

export type TipoImovel =
  | 'Casa'
  | 'Apartamento'
  | 'Comercio'
  | 'Industria'
  | 'Rural'
  | 'Outro'

export interface ErroResponse {
  timestamp: string
  status: number
  erro: string
  mensagem: string
  campos: Record<string, string>
}

export class ApiError extends Error {
  campos: Record<string, string>

  constructor(mensagem: string, campos: Record<string, string>) {
    super(mensagem)
    this.name = 'ApiError'
    this.campos = campos
  }
}
