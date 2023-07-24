import { Navbar, Container, Nav } from 'react-bootstrap'

import { Outlet, NavLink } from 'react-router-dom'

import './Navbar.css'

import 'bootstrap/dist/css/bootstrap.min.css'

function NavBar() {
  return (
    <>
      <Navbar variant='light' expand={true}>
        <Container>
          <Navbar.Collapse id='basic-navbar-nav'>
            <Nav className='ml-auto'>
              <NavLink
                to='/games/create'
                className='nav-link'
                activeclassname='active'>
                Crear Partida
              </NavLink>
              <NavLink
                to='/games/list'
                className='nav-link'
                activeclassname='active'>
                Partidas Disponibles
              </NavLink>
              <NavLink
                to='/player'
                className='nav-link'
                activeclassname='active'>
                Datos Personales
              </NavLink>

              <NavLink to='/logout' className='nav-link'>
                Cerrar Sesion
              </NavLink>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>

      <section>
        <Outlet></Outlet>
      </section>
    </>
  )
}

export default NavBar
