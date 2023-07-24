import React, { useEffect, useState } from 'react'

import logo from '../../logo.svg'
import './EditPlayer.css'
import axios from 'axios'
import NavBar from '../../Navbar'
import tokenService from '../../services/tokenService'

const apiUrl = process.env.REACT_APP_API_URL

function EditPlayer() {
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    telephone: '',
    user: { username: '', password: '' },
  })

  const [error, setError] = useState(false)

  const [errorMsg, setErrorMsg] = useState(false)

  useEffect(() => {
    player()
  }, [])

  const player = () => {
    const token = tokenService.getLocalAccessToken()

    const url = apiUrl + '/player/data'

    axios
      .get(url, {
        headers: {
          Authorization: `Bearer  ${token}`,
        },
      })
      .then(response => {
        const player = response.data

        setForm({
          firstName: player.firstName,
          lastName: player.lastName,
          telephone: player.telephone,
          user: {
            username: player.user.username,
            password: player.user.password,
          },
        })
      })
  }

  const handleSubmit = e => {
    e.preventDefault()
  }

  const handleChange = e => {
    if (e.target.name === 'username' || e.target.name === 'password') {
      setForm({
        ...form,
        user: { ...form.user, [e.target.name]: e.target.value },
      })
    } else {
      setForm({ ...form, [e.target.name]: e.target.value })
    }
  }

  const handleButton = e => {
    const token = tokenService.getLocalAccessToken()

    const url = apiUrl + '/player/update'

    if (validate(form)) {
      axios
        .post(url, form, {
          headers: {
            Authorization: `Bearer  ${token}`,
          },
        })
        .then(response => {
          console.log(response)
          if (response.status === 200) {
            if (response.data.jwtToken) {
              localStorage.setItem('jwt', response.data.jwtToken)
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
    <React.Fragment>
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
              name='firstName'
              placeholder='Nombre'
              value={form.firstName}
              required
              onChange={handleChange}
            />
            <input
              type='text'
              className='fadeIn second'
              name='lastName'
              placeholder='Apellidos'
              value={form.lastName}
              required
              onChange={handleChange}
            />
            <input
              type='text'
              className='fadeIn second'
              name='telephone'
              placeholder='Telefono'
              value={form.telephone}
              required
              onChange={handleChange}
            />
            <input
              type='text'
              className='fadeIn second'
              name='username'
              placeholder='Usuario'
              value={form.user.username}
              required
              onChange={handleChange}
            />
            <input
              type='password'
              className='fadeIn third'
              name='password'
              placeholder='Contraseña'
              value={form.user.password}
              required
              onChange={handleChange}
            />
            <input type='submit' value='Actualizar' onClick={handleButton} />
          </form>

          {error === true && (
            <div className='alert alert-danger' role='alert'>
              {errorMsg}
            </div>
          )}
        </div>
      </div>
    </React.Fragment>
  )
}

function validate(props) {
  return (
    props.firstName.length > 1 &&
    props.lastName.length > 1 &&
    props.telephone.length > 1
  )
}

export default EditPlayer
