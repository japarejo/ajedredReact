import React from 'react';

import axios from 'axios';

import { useLocation, useNavigate } from "react-router-dom";

import { envLoader } from '../../env/envLoader';


import NavBar from '../../Navbar';



class AwaitGame extends React.Component{

    componentDidMount = () => {
        const direccion = this.props.location.pathname;
        const socket = new WebSocket('ws://localhost:8080' + direccion.substring(0,direccion.lastIndexOf("/")) + '/ws');

        socket.onmessage = (event) => {
            window.location.replace(direccion.substring(0,direccion.lastIndexOf("/")));
        }
        
    }

    

    render(){
    return(
        <React.Fragment>
            <NavBar></NavBar>
        <div>
            <h1>Esperando a que se una otro jugador</h1>
        </div>
        </React.Fragment>
        )

    }
}


export default function Redirect(props){
    const location = useLocation();
    const navigate = useNavigate();
  
    return <AwaitGame {... props} location={location} navigate={navigate}/>;

}