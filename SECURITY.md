# Política de Segurança

## Versões Suportadas

Atualmente o GambIA está em fase de protótipo (v0.1.0). Apenas a versão
mais recente da branch `main` recebe atualizações de segurança.

| Versão | Suportada |
|--------|-----------|
| main (último commit) | Sim |
| dev | Apenas para desenvolvimento |
| < 0.1.0 | Não |

## Reportando Vulnerabilidades

Se você descobrir uma vulnerabilidade de segurança no GambIA, por favor
reporte através dos canais abaixo. **Não abra uma issue pública** para
vulnerabilidades de segurança.

### Canais para Reporte

- **Email**: [joseanderson.dev@email.com](mailto:joseanderson.dev@email.com)
- **LinkedIn**: [in/dessima](https://linkedin.com/in/dessima)

### O que incluir no reporte

- Descrição clara do problema
- Passos para reproduzir
- Impacto potencial
- Sugestão de correção (se houver)

### Política de Divulgação

Após o reporte:

1. Confirmaremos o recebimento em até 48 horas
2. Investigaremos e trataremos o problema
3. Manteremos você informado sobre o progresso
4. Publicaremos uma correção assim que disponível

## Práticas de Segurança Atuais

- JWT armazenado em cookie httpOnly (não acessível via JavaScript)
- CSRF Double Submit Cookie com cookie `XSRF-TOKEN` não-httpOnly
- CORS restrito a origens conhecidas
- Sessões stateless (sem sessão no servidor)
- Validação de entrada com Bean Validation (backend)
- Validação de dados com Pydantic (ML service)
- PostgreSQL com usuário e senha configurados via ambiente
- Dependências gerenciadas via Maven, npm e pip com versões fixadas

## Dependências

Mantenha as dependências atualizadas executando periodicamente:

```bash
# Backend
cd backend && mvn versions:display-dependency-updates

# Frontend
cd frontend && npm outdated

# ML Service
cd ml-service && pip list --outdated
```
