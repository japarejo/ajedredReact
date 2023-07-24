import { useEffect, useState } from 'react'
import axios from 'axios'
import './EditPlayer.css'
import logo from '../../logo.svg'
import NavBar from '../../Navbar'
import tokenService from '../../services/tokenService'

function EditEvaluationContext() {
  const evaluationContext = {
    allow_game_spectators: "planContext['allow_game_spectators']",
    max_games: "userContext[games_played] <= planContext['max_games']",
  }

  const [form, setForm] = useState(evaluationContext)

  const [error, setError] = useState(false)

  const [errorMsg, setErrorMsg] = useState(false)

  useEffect(() => {
    player()
  }, [])

  const player = () => {
    const token = tokenService.getLocalAccessToken()

    const evaluationContextUrl =
      process.env.API_URL + '/admin/evaluation-context'

    axios
      .get(evaluationContextUrl, {
        headers: {
          Authorization: `Bearer  ${token}`,
        },
      })
      .then(response => {
        const player = response.data

        setForm({
          allow_game_spectators: player.allow_game_spectators,
          max_games: player.max_games,
        })
      })
  }

  const handleChange = async e => {
    if (e.target.name === 'username' || e.target.name === 'password') {
      setForm({
        ...form,
        user: { ...form.user, [e.target.name]: e.target.value },
      })
    } else {
      setForm({ ...form, [e.target.name]: e.target.value })
    }
  }

  const handleSubmit = e => {
    e.preventDefault()
    const token = tokenService.getLocalAccessToken()

    const playerUpdateUrl = process.env.API_URL + '/player/update'

    if (validate(form)) {
      axios
        .post(playerUpdateUrl, form, {
          headers: {
            Authorization: `Bearer  ${token}`,
          },
        })
        .then(response => {
          console.log(response)
          if (response.status === 200) {
            if (response.data.jwtToken) {
              tokenService.updateLocalAccessToken(response.data.jwtToken)
            }
            alert('Se han modificado los datos correctamente')
            window.location.replace('/games/list')
          }
        })
        .catch(error => {
          setError(true)
          setErrorMsg('El nombre de usuario ya existe')
          console.log(error)
        })
    } else {
      setError(true)
      setErrorMsg('La longitud de los campos debe ser mayor que 1')
    }
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
              name='allow_game_spectators'
              placeholder='Allow games spectators condition'
              value={form.allow_game_spectators}
              required
              onChange={handleChange}
            />
            <input
              type='text'
              className='fadeIn second'
              name='max_games'
              placeholder='Max games condition'
              value={form.max_games}
              required
              onChange={handleChange}
            />
            <input type='submit' value='Actualizar' />
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

function validate(props) {
  return props.allow_game_spectators.length > 1 && props.max_games.length > 1
}

export default EditEvaluationContext
