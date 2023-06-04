import React, { useEffect } from 'react';

import axios from 'axios';

import { useLocation, useNavigate } from "react-router-dom";

import { envLoader } from '../../env/envLoader';


import NavBar from '../../Navbar';

const apiUrl = "http://localhost:8080/api";

function AwaitGame() {

   useEffect(() =>{
    setInterval(numberOfPlayers,1000);
   },[])
 
    
   const sampleLocation = useLocation();
   
   
    const numberOfPlayers = () =>{
            
            const token = localStorage.getItem("jwtToken");
    
            let url = apiUrl + sampleLocation.pathname;
        
            
            axios.get(url,
                {
                    headers: {
                    "Authorization": `Bearer  ${token}`
                    }
    
                }).then( response =>{
                    if(response.data===2){
                        const direccion = sampleLocation.pathname;
                        window.location.replace(direccion.substring(0,direccion.lastIndexOf("/")));
                    }
                    
    
                    }
    
                    )
            }
 

    

    return(
        <React.Fragment>
            <NavBar></NavBar>
        <div>
            <h1>Esperando a que se una otro jugador</h1>
        </div>
        </React.Fragment>
        )

}


export default AwaitGame;