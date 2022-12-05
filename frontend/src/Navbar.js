import React from 'react';


import { Navbar, Container } from "react-bootstrap"

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
                 
                <Navbar.Brand as={Link} to="/games/list" >Partidas Disponibles</Navbar.Brand>
                <Navbar.Brand as={Link} to="/player">Datos Personales</Navbar.Brand>                
                
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