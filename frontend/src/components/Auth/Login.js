import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import './Login.css'

import logo from '../../logo.svg'
import axios from 'axios'

import 'bootstrap/dist/css/bootstrap.min.css'

import tokenService from '../../services/tokenService'

const apiUrl = process.env.REACT_APP_API_URL

function Login() {
  const [form, setForm] = useState({
    username: '',
    password: '',
  })

  const [error, setError] = useState(false)

  const [errorMsg, setErrorMsg] = useState(false)
  const navigate = useNavigate()

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = e => {
    e.preventDefault()
    const url = apiUrl + '/login'
    if (validate(form)) {
      axios
        .post(url, form)
        .then(response => {
          if (response.status === 200) {
            tokenService.updateLocalAccessToken(response.data)
            navigate('/games/list')
          }
        })
        .catch(error => {
          setError(true)
          setErrorMsg('Credenciales Incorrectas')
          console.error(error)
        })
    } else {
      setError(true)
      setErrorMsg('La longitud de los campos debe ser mayor que 1')
    }
  }

  return (
    <div className='wrapper fadeInDown'>
      <div id='formContent'>
        <div className='fadeIn first'>
          <img src={logo} width='100px' alt='User Icon' />
        </div>

        <form onSubmit={handleSubmit}>
          <input
            type='text'
            className='fadeIn second'
            name='username'
            placeholder='Usuario'
            required
            onChange={handleChange}
          />
          <input
            type='password'
            className='fadeIn third'
            name='password'
            placeholder='Contraseña'
            required
            onChange={handleChange}
          />
          <input
            type='submit'
            className='fadeIn fourth'
            value='Iniciar Sesion'
          />
        </form>

        {error === true && (
          <div className='alert alert-danger' role='alert'>
            {errorMsg}
          </div>
        )}
      </div>
    </div>
  )
}

function validate(props) {
  return props.username.length > 1 && props.password.length > 1
}

export default Login
