import { useEffect } from 'react'
import tokenService from '../../services/tokenService'
import { Navigate } from 'react-router-dom'

function Logout() {
  useEffect(() => {
    tokenService.removeUser()
  }, [])

  return <Navigate to='/' replace />
}

export default Logout
