# Frontend - React / TypeScript / Vite

## Estrutura

```
frontend/src/
+-- main.tsx                     # Entry point: BrowserRouter + ThemeProvider
+-- App.tsx                      # Rotas: /, /login, /cadastrar
+-- App.css                      # Tema Energia Verde (CSS custom properties)
+-- vite-env.d.ts
+-- context/
|   +-- ThemeContext.tsx          # Tema claro/escuro com localStorage
+-- types/
|   +-- index.ts                 # Interfaces TypeScript
+-- services/
|   +-- demo.ts                  # Chamada pública (sem auth)
|   +-- api.ts                   # Chamada autenticada (com CSRF + cookies)
+-- components/
|   +-- Logo.tsx                 # SVG personalizado (árvore + letra G)
|   +-- Navbar.tsx               # Navegação com toggle de tema
|   +-- Hero.tsx                 # Seção hero com gradientes animados
|   +-- DemoTool.tsx             # Formulário de demonstração
|   +-- FeatureCards.tsx         # Bento grid de funcionalidades
|   +-- Footer.tsx               # Rodapé
+-- pages/
    +-- Home.tsx                 # Landing page completa
    +-- Login.tsx                # Placeholder "Em Breve"
    +-- Cadastrar.tsx            # Placeholder "Em Breve"
```

## Rotas

| Rota | Página | Descrição |
|------|--------|-----------|
| `/` | Home | Landing page com Hero + Demo + Features + Footer |
| `/login` | Login | Placeholder com "Em Breve" (inputs disabled) |
| `/cadastrar` | Cadastrar | Placeholder com "Em Breve" (inputs disabled) |

## Temas (Claro/Escuro)

### ThemeContext

- Persiste preferência em `localStorage` com chave `gambia-theme`
- Detecta `prefers-color-scheme` como fallback inicial
- Define atributo `data-theme` no `<html>`

### Cores do Tema "Energia Verde"

| Variável | Claro | Escuro |
|----------|-------|--------|
| `--primary` | `#059669` (emerald-600) | `#34d399` (emerald-400) |
| `--bg` | `#f9fafb` (gray-50) | `#0a0f1e` (slate-950) |
| `--surface` | `#ffffff` | `#131c31` |
| `--text` | `#111827` (gray-900) | `#f1f5f9` (slate-100) |
| `--accent` | `#f59e0b` (amber-500) | `#fbbf24` (amber-400) |

### Componentes com transição suave

- Navbar com glassmorphism (`backdrop-filter: blur(16px)`)
- Hero com gradientes radiais animados e formas flutuantes
- Feature Cards com bento grid (primeiro card ocupa 2 colunas)
- Footer escuro fixo

## Logo (SVG)

`Logo.tsx` renderiza um SVG de 120x120 contendo:

- Canopy: 3 círculos verdes sobrepostos simulando copa de árvore
- Destaques internos em verde claro
- Letra "G" em negativo (branco) no centro da copa
- Tronco marrom (`#047857`)
- Raízes laterais

Também exporta `logoFaviconSvg()` para uso no `index.html`.

## DemoTool

Formulário público de demonstração com:

| Campo | Tipo | Default |
|-------|------|---------|
| consumo_kwh | input number (min 0, step 0.01) | 300 |
| tipo_imovel | select (6 opções) | Casa |
| quantidade_equipamentos | input number (1-100) | 8 |
| uso_horario_pico | checkbox | false |

Acesso avulso de `horas_alto_consumo` é feito via o state interno do form,
embora o campo seja enviado sempre como 6 (hardcoded no state inicial).

Após submit, exibe:

- Badge colorido com a categoria (verde/amarelo/vermelho)
- Confiança em percentual
- Custo estimado em R$
- Lista de recomendações com borda verde

## Services

### demo.ts (Público)

```typescript
async function analisarDemo(data: AnaliseRequest): Promise<AnaliseResponse>
```

- Chama `POST /analise-energetica` sem headers de autenticação
- Usa `VITE_API_URL` como base (default `http://localhost:8080`)
- Lança erro com mensagem do backend em caso de falha

### api.ts (Autenticado)

```typescript
async function analisarEnergia(data: AnaliseRequest): Promise<AnaliseResponse>
async function login(): Promise<void>
```

- Lê cookie `XSRF-TOKEN` e envia como header `X-XSRF-TOKEN`
- Inclui `credentials: include` para cookies de sessão
- `login()` faz GET para `/login` para obter cookie JWT

## Tipos Compartilhados

```typescript
interface AnaliseRequest {
  imovel_id?: string
  consumo_kwh: number
  uso_horario_pico: boolean
  quantidade_equipamentos: number
  tipo_imovel: TipoImovel
  horas_alto_consumo: number
}

interface AnaliseResponse {
  categoria: ClassificacaoEficiencia
  probabilidade: number
  recomendacoes: string[]
  custo_estimado_mensal: number
}

type ClassificacaoEficiencia = 'Eficiente' | 'Moderado' | 'Ineficiente'
type TipoImovel = 'Casa' | 'Apartamento' | 'Comercio' | 'Industria' | 'Rural' | 'Outro'
```
