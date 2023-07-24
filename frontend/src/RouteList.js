import { Routes, Route, BrowserRouter, Navigate } from 'react-router-dom'
import Welcome from './Welcome'
import Login from './components/Auth/Login'
import Register from './components/Auth/Register'
import CreateGame from './components/Games/CreateGame'
import AwaitGame from './components/Games/AwaitGame'
import Game from './components/Games/Game'
import ListGame from './components/Games/ListGame'
import EditGame from './components/Player/EditPlayer'
import Logout from './components/Auth/Logout'
import axios from 'axios'

import { useState, useEffect } from 'react'
import tokenService from './services/tokenService'

const apiUrl = process.env.REACT_APP_API_URL

export const RouteList = () => {
  const esTokenValido = async token => {
    try {
      const url = apiUrl + '/auth/validate_token/' + token
      const response = await axios.get(url)
      return response.status === 200
    } catch (error) {
      console.error('Error al validar token:', error)
      return false
    }
  }

  const HandleAuth = ({ component: Component }) => {
    const [esValido, setEsValido] = useState(null)

    useEffect(() => {
      const validar = async () => {
        const token = tokenService.getLocalAccessToken()
        const resultado = await esTokenValido(token)
        setEsValido(resultado)
      }
      validar()
    }, [])

    if (esValido === null) {
      return <></>
    } else if (esValido) {
      return <Component />
    } else {
      return <Navigate to='/login' replace />
    }
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Welcome />} />
        <Route path='/login' element={<Login />} />
        <Route path='/register' element={<Register />} />
        <Route path='/logout' element={<Logout />} />
        <Route
          path='/games/list'
          element={<HandleAuth component={ListGame} />}
        />
        <Route
          path='/games/create'
          element={<HandleAuth component={CreateGame} />}
        />
        <Route
          path='/games/:id/awaitGame'
          element={<HandleAuth component={AwaitGame} />}
        />
        <Route path='/games/:id' element={<HandleAuth component={Game} />} />
        <Route path='/player' element={<HandleAuth component={EditGame} />} />
        <Route path='*' element={<Navigate to='/login' replace />} />
      </Routes>
    </BrowserRouter>
  )
}
