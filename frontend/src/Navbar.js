import React from 'react';

import './Navbar.css'

const Navbar = () =>{
    return(
        <nav className="navbar navbar-fixed-top navbar-expand-lg navbar-light bg-light ">
        <div className="navbar-collapse collapse w-100 order-1 order-md-0 dual-collapse2">
            <ul className="navbar-nav mr-auto">
                <li className="nav-item dropdown">
                    <a className="nav-link dropdown-toggle"  href = "/" role="navbardrop" data-toggle="dropdown">Partidas</a>
            <div className="dropdown-menu">
              <a className="dropdown-item" href="/games/create">Crear Partida</a>
                    <a className="dropdown-item" href="/cursosdisponibles">Partidas disponibles</a>
            </div>
          </li>
          </ul>
        </div>
        </nav>
    )
}


export default Navbar