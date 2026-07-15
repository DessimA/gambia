import { Link } from 'react-router-dom'
import Logo from '../components/Logo'

export default function Cadastrar() {
  return (
    <main className="auth-page">
      <div className="auth-card">
        <div className="auth-header">
          <Logo size={40} />
          <h1>GambIA</h1>
          <p>Crie sua conta</p>
        </div>

        <div className="auth-coming-soon">
          <span className="coming-soon-badge">Em Breve</span>
          <p>
            O cadastro de usuários será ativado em breve. Você poderá salvar suas
            análises, acompanhar o histórico e receber recomendações personalizadas.
          </p>
        </div>

        <form className="auth-form" onSubmit={(e) => e.preventDefault()}>
          <div className="form-group">
            <label htmlFor="nome">Nome completo</label>
            <input id="nome" type="text" placeholder="Seu nome" disabled />
          </div>
          <div className="form-group">
            <label htmlFor="email">E-mail</label>
            <input id="email" type="email" placeholder="seu@email.com" disabled />
          </div>
          <div className="form-group">
            <label htmlFor="senha">Senha</label>
            <input id="senha" type="password" placeholder=".........." disabled />
          </div>
          <div className="form-group">
            <label htmlFor="confirmar">Confirmar senha</label>
            <input id="confirmar" type="password" placeholder=".........." disabled />
          </div>
          <button type="submit" className="btn btn-primary btn-full" disabled>
            Cadastrar
          </button>
        </form>

        <p className="auth-footer-text">
          Já tem conta?{' '}
          <Link to="/login" className="auth-link">Faça login</Link>
        </p>

        <Link to="/" className="auth-back">Voltar ao início</Link>
      </div>
    </main>
  )
}
