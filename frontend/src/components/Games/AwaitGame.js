import React from 'react';

import axios from 'axios';

import { useLocation, useNavigate } from "react-router-dom";

import NavBar from '../../Navbar';


class AwaitGame extends React.Component{

    componentDidMount = () => {
       setInterval(this.numberOfPlayers,1000);
        
    }

    numberOfPlayers = () =>{
        
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080" + this.props.location.pathname;
      
       
        axios.get(url,
            {
                headers: {
                "Authorization": `Bearer  ${token}`
                }

            }).then( response =>{
                if(response.data===2){
                    const direccion = this.props.location.pathname;
                    window.location.replace(direccion.substring(0,direccion.lastIndexOf("/")));
                }
                

                }

                )
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