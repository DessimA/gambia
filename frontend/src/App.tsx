import { Routes, Route } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import Navbar from './components/Navbar'
import ScrollToTop from './components/ScrollToTop'
import Home from './pages/Home'
import Login from './pages/Login'
import Cadastrar from './pages/Cadastrar'
import './App.css'

export default function App() {
  return (
    <AuthProvider>
      <Navbar />
      <ScrollToTop />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/cadastrar" element={<Cadastrar />} />
      </Routes>
    </AuthProvider>
  )
}
