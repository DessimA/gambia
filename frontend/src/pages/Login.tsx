import { Link } from 'react-router-dom'
import Logo from '../components/Logo'

export default function Login() {
  return (
    <main className="auth-page">
      <div className="auth-card">
        <div className="auth-header">
          <Logo size={40} />
          <h1>GambIA</h1>
          <p>Entre na sua conta</p>
        </div>

        <div className="auth-coming-soon">
          <span className="coming-soon-badge">Em Breve</span>
          <p>
            O sistema de autenticação está sendo implementado. Em breve você poderá
            criar sua conta, salvar seu histórico de análises e acompanhar sua
            evolução no consumo de energia.
          </p>
        </div>

        <form className="auth-form" onSubmit={(e) => e.preventDefault()}>
          <div className="form-group">
            <label htmlFor="email">E-mail</label>
            <input id="email" type="email" placeholder="seu@email.com" disabled />
          </div>
          <div className="form-group">
            <label htmlFor="senha">Senha</label>
            <input id="senha" type="password" placeholder=".........." disabled />
          </div>
          <button type="submit" className="btn btn-primary btn-full" disabled>
            Entrar
          </button>
        </form>

        <p className="auth-footer-text">
          Não tem conta?{' '}
          <Link to="/cadastrar" className="auth-link">Cadastre-se</Link>
        </p>

        <Link to="/" className="auth-back">Voltar ao início</Link>
      </div>
    </main>
  )
}
