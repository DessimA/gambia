import { useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { Menu, Sun, Moon } from 'lucide-react'
import { useTheme } from '../context/ThemeContext'
import Logo from './Logo'

export default function Navbar() {
  const [menuOpen, setMenuOpen] = useState(false)
  const { theme, toggle } = useTheme()
  const location = useLocation()

  const links = [
    { to: '/', label: 'Início' },
    { to: '/login', label: 'Login' },
    { to: '/cadastrar', label: 'Cadastrar' },
  ]

  return (
    <nav className="navbar">
      <div className="navbar-inner">
        <Link to="/" className="navbar-logo">
          <Logo size={26} />
          <span className="navbar-logo-text">GambIA</span>
        </Link>

        <div className="navbar-right">
          <button
            className="theme-toggle"
            onClick={toggle}
            aria-label={theme === 'light' ? 'Modo escuro' : 'Modo claro'}
          >
            {theme === 'light' ? <Moon size={18} /> : <Sun size={18} />}
          </button>

          <button
            className={`navbar-toggle ${menuOpen ? 'open' : ''}`}
            onClick={() => setMenuOpen(!menuOpen)}
            aria-label="Menu"
          >
            <Menu size={24} />
          </button>
        </div>

        <div className={`navbar-links ${menuOpen ? 'visible' : ''}`}>
          {links.map((link) => (
            <Link
              key={link.to}
              to={link.to}
              className={`navbar-link ${location.pathname === link.to ? 'active' : ''}`}
              onClick={() => setMenuOpen(false)}
            >
              {link.label}
            </Link>
          ))}
          <button
            className="theme-toggle mobile-only"
            onClick={toggle}
            aria-label={theme === 'light' ? 'Modo escuro' : 'Modo claro'}
          >
            {theme === 'light' ? <Moon size={18} /> : <Sun size={18} />}
            <span>{theme === 'light' ? 'Modo escuro' : 'Modo claro'}</span>
          </button>
        </div>
      </div>
    </nav>
  )
}
