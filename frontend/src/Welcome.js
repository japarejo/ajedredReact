import 'bootstrap/dist/css/bootstrap.min.css'
import { Link } from 'react-router-dom'

export default function Welcome() {
  return (
    <div className='container p-5 text-center'>
      <h1 className='col h1 m-3 font-weight-normal text-center'>
        Entra y disfruta del ajedrez
      </h1>

      <div className='row'>
        <Link className='col btn btn-lg btn-primary p-2 m-1' to='/login'>
          Iniciar sesión
        </Link>
        <Link className='col btn btn-lg btn-primary p-2  m-1' to='/register'>
          Registrarse
        </Link>
      </div>
    </div>
  )
}
