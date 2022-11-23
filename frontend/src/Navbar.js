import React from 'react';


import { Navbar, Nav, Container } from "react-bootstrap"

import { Outlet, Link } from "react-router-dom"

import './Navbar.css';

import 'bootstrap/dist/css/bootstrap.min.css';

class NavBar extends React.Component{

    render(){
        return(
            <>    
            <Navbar className="navbar" variant="white" expand="lg">
             <Container>
                 <Navbar.Brand as={Link} to="/games/create" >Crear Partida</Navbar.Brand>
                 <Navbar.Toggle aria-controls="basic-navbar-nav" />
                 <Navbar.Collapse id="basic-navbar-nav">
                 <Nav className="nav-item dropdown">
                     <Nav.Link as={Link} to="/games/list" >Partidas Disponibles</Nav.Link>
                     <Nav.Link as={Link} to="/contact">Datos Personales</Nav.Link>                
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
}


export default NavBar