import React from 'react';

import axios from 'axios';

import NavBar from '../../Navbar';

import 'bootstrap/dist/css/bootstrap.min.css';

import { Button } from 'reactstrap';

class ListGame extends React.Component{

    state = {
        games:[]
    };

    
  

    componentDidMount = () => {
       this.listGames()
       setInterval(this.listGames,500);
        
    }

    unirsePartida(id){
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080/games/" + id + "/join";
      
       
        axios.get(url,
            {
                headers: {
                "Authorization": `Bearer  ${token}`
                }

            }).then( response =>{
                if(response.data==='OK'){
                    window.location.replace("/games/" + id);
                }
                })
        }

    
    espectador(id){
        const token = localStorage.getItem("jwtToken");
    
        let url = "http://localhost:8080/games/" + id + "/awaitGame";
          
           
        axios.get(url,
            {
                headers: {
                "Authorization": `Bearer  ${token}`
                }
    
            }).then( response =>{
                if(response.data===2){
                    window.location.replace("/games/" + id);
                }else{
                    window.location.replace("/games/" + id + "/awaitGame");
                }
                })
        }


    listGames = () =>{
        
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080/games/findGames";
      
       
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
                               {value.numeroJugadores===1?<Button color="primary" onClick={() => this.unirsePartida(value.id)}> Unirse</Button>:''}
                               {value.espectadores===true?<Button color="success" onClick={() => this.espectador(value.id)}> Espectador</Button>:''}
                            </td>
                            
                        </tr>
                        
                    )
                    
                })}
            </tbody>
        </table>
        </div>

        </React.Fragment>
        )

    }
}

export default ListGame;