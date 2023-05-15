import React from 'react';

import axios from 'axios';

import NavBar from '../../Navbar';

import 'bootstrap/dist/css/bootstrap.min.css';

import { envLoader } from '../../env/envLoader';

import Cookies from 'js-cookie';


import { Button } from 'reactstrap';

const apiUrl = "http://localhost:8080/api";

class ListGame extends React.Component{

    state = {
        games:[]
    };

    
  

    componentDidMount = () => {
       this.listGames()
       setInterval(this.listGames,500);
        
    }

    unirsePartida(id,tiempo){
        const token = localStorage.getItem("jwtToken");

        let url = apiUrl + "/games/" + id + "/join";
      
       
        axios.get(url,
            {
                headers: {
                "Authorization": `Bearer  ${token}`
                }

            }).then( response =>{
                if(response.data==='OK'){
                    Cookies.set("time",tiempo * 60);
                    Cookies.set("timeOpponent",tiempo*60);
                    window.location.replace("/games/" + id);
                }
                })
        }

    
    espectador(id,tiempo){
        const token = localStorage.getItem("jwtToken");
    
        let url = apiUrl + "/games/" + id + "/awaitGame";
          
           
        axios.get(url,{headers: {"Authorization": `Bearer  ${token}`}})
            .then( response =>{
                
                if(response.data===2){
                    let url = apiUrl + "/games/" + id + "/espectador";

                    axios.get(url,{headers: {"Authorization": `Bearer  ${token}`}})
                    .then( response =>{

                        Cookies.set("time",response.data[0]);
                        Cookies.set("timeOpponent", response.data[1]);

                        window.location.replace("/games/" + id);

                    })
                }else{

                    Cookies.set("time",tiempo * 60);
                    Cookies.set("timeOpponent",tiempo*60);

                    window.location.replace("/games/" + id + "/awaitGame");
                }
                })
        }


    listGames = () =>{
        
        const token = localStorage.getItem("jwtToken");

        let url = apiUrl + "/games/findGames";
      
       
        axios.get(url,
            {
                headers: {
                "Authorization": `Bearer  ${token}`
                }

            }).then( response =>{
                this.setState({
                    games : response.data
                })
                
                })
        }

    

    render(){
    return(

        <React.Fragment>
        <NavBar></NavBar>
        <div className="container">
        {this.state.games.length === 0 &&
            <div>
                <br></br>
                
                <h1>No hay partidas disponibles en este momento</h1>

            </div>
        }

        {this.state.games.length > 0 &&
        <table className="table table-hover">

            <thead>
                <tr>
                    <th scope="col">Nombre</th>
                    <th scope="col">Tiempo</th>
                    <th scope="col">Acciones</th>
                    
                </tr>
            </thead>

            <tbody>
                { this.state.games.map((value,i) =>{
                    return (
                        <tr key={i}>
                            <td>{value.name} </td>
                            <td>{value.tiempo} min</td>
                            
                           
                            <td>
                               {value.numeroJugadores===1?<Button color="primary" onClick={() => this.unirsePartida(value.id,value.tiempo)}> Unirse</Button>:''}
                               {value.espectadores===true?<Button color="success" onClick={() => this.espectador(value.id,value.tiempo)}> Espectador</Button>:''}
                            </td>
                            
                        </tr>
                        
                    )
                    
                })}
            </tbody>
        </table>
    }
        </div>

        </React.Fragment>
        )

    }
}

export default ListGame;