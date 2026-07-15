# Politica de Seguranca

## Versoes Suportadas

Atualmente o GambIA esta em fase de prototipo (v0.1.0). Apenas a versao
mais recente da branch `main` recebe atualizacoes de seguranca.

| Versao | Suportada |
|--------|-----------|
| main (ultimo commit) | Sim |
| dev | Apenas para desenvolvimento |
| < 0.1.0 | Nao |

## Reportando Vulnerabilidades

Se voce descobrir uma vulnerabilidade de seguranca no GambIA, por favor
reporte atraves dos canais abaixo. **Nao abra uma issue publica** para
vulnerabilidades de seguranca.

### Canais para Reporte

- **Email**: [joseanderson.dev@email.com](mailto:joseanderson.dev@email.com)
- **LinkedIn**: [in/dessima](https://linkedin.com/in/dessima)

### O que incluir no reporte

- Descricao clara do problema
- Passos para reproduzir
- Impacto potencial
- Sugestao de correcao (se houver)

### Politica de Divulgacao

Apos o reporte:

1. Confirmaremos o recebimento em ate 48 horas
2. Investigaremos e trataremos o problema
3. Manteremos voce informado sobre o progresso
4. Publicaremos uma correcao assim que disponivel

## Praticas de Seguranca Atuais

- JWT armazenado em cookie httpOnly (nao acessivel via JavaScript)
- CSRF Double Submit Cookie com cookie `XSRF-TOKEN` nao-httpOnly
- CORS restrito a origens conhecidas
- Sessoes stateless (sem sessao no servidor)
- Validacao de entrada com Bean Validation (backend)
- Validacao de dados com Pydantic (ML service)
- PostgreSQL com usuario e senha configurados via ambiente
- Dependencias gerenciadas via Maven, npm e pip com versoes fixadas

## Dependencias

Mantenha as dependencias atualizadas executando periodicamente:

```bash
# Backend
cd backend && mvn versions:display-dependency-updates

# Frontend
cd frontend && npm outdated

# ML Service
cd ml-service && pip list --outdated
```
