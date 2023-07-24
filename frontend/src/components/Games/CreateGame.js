import { useState } from 'react'

import './CreateGame.css'

import logo from '../../logo.svg'
import axios from 'axios'

import Cookies from 'js-cookie'

import NavBar from '../../Navbar'
import { Default, Feature, feature, On } from 'pricingplans-react'
import { Alert } from 'react-bootstrap'
import tokenService from '../../services/tokenService'

const apiUrl = process.env.REACT_APP_API_URL

function CreateGame() {
  const [form, setForm] = useState({
    name: '',
    tiempo: '3',
    espectadores: 'True',
  })

  const [error, setError] = useState(false)

  const [errorMsg, setErrorMsg] = useState('')

  const handleSubmit = e => {
    e.preventDefault()
  }

  const handleChange = async e => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleButton = () => {
    const token = tokenService.getLocalAccessToken()

    const url = apiUrl + '/games/create'

    axios
      .post(url, form, {
        headers: {
          Authorization: `Bearer  ${token}`,
        },
      })
      .then(response => {
        if (response.status === 200) {
          const id = response.data

          Cookies.set('time', form.tiempo * 60)
          Cookies.set('timeOpponent', form.tiempo * 60)
          window.location.replace('/games/' + id + '/awaitGame')
        }
      })
      .catch(error => {
        setError(true)
        setErrorMsg('Ese nombre de partida ya ha sido utilizado')
        console.log(error)
      })
  }

  return (
    <>
      <NavBar></NavBar>

      <div className='wrapper fadeInDown'>
        <div id='formContent'>
          <div className='fadeIn first'>
            <img src={logo} width='100px' alt='User Icon' />
          </div>

          <form onSubmit={handleSubmit}>
            <input
              type='text'
              className='fadeIn second'
              name='name'
              placeholder='Nombre'
              required
              onChange={handleChange}
            />
            <label className='m-4 p-1'>
              Tiempo Movimiento:
              <select
                className='m-1'
                name='tiempo'
                value={form.tiempo}
                onChange={handleChange}>
                <option value='3'>3 min</option>
                <option value='5'>5 min</option>
                <option value='10'>10 min</option>
              </select>
            </label>

            <Feature>
              <On expression={feature('allow_game_spectators')}>
                <label>
                  Espectadores:
                  <select
                    name='espectadores'
                    value={form.espectadores}
                    onChange={handleChange}>
                    <option value='True'>Si</option>
                    <option value='False'>No</option>
                  </select>
                </label>
              </On>
              <Default>
                <Alert className='m-2' variant='info'>
                  <p>
                    If you want to have spectators consider to upgrade to
                    <b> PRO </b> plan
                  </p>
                </Alert>
              </Default>
            </Feature>

            <input
              type='submit'
              className='fadeIn fourth'
              value='Crear Partida'
              onClick={handleButton}
            />
          </form>

          {error === true && (
            <div className='alert alert-danger' role='alert'>
              {errorMsg}
            </div>
          )}
        </div>
      </div>
    </>
  )
}

export default CreateGame
