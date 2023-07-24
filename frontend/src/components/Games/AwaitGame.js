import { useEffect } from 'react'

import axios from 'axios'

import { useLocation } from 'react-router-dom'

import NavBar from '../../Navbar'
import tokenService from '../../services/tokenService'

const apiUrl = process.env.REACT_APP_API_URL

function AwaitGame() {
  useEffect(() => {
    setInterval(numberOfPlayers, 1000)
  }, [])

  const sampleLocation = useLocation()

  const numberOfPlayers = () => {
    const token = tokenService.getLocalAccessToken()
    const url = apiUrl + sampleLocation.pathname

    axios
      .get(url, {
        headers: {
          Authorization: `Bearer  ${token}`,
        },
      })
      .then(response => {
        if (response.data === 2) {
          const direccion = sampleLocation.pathname
          window.location.replace(
            direccion.substring(0, direccion.lastIndexOf('/')),
          )
        }
      })
  }

  return (
    <>
      <NavBar></NavBar>
      <h1>Esperando a que se una otro jugador</h1>
    </>
  )
}

export default AwaitGame
