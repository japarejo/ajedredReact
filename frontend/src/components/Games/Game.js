import React from 'react';

import axios from 'axios';

import { useLocation} from "react-router-dom"

import NavBar from '../../Navbar';


class Game extends React.Component{

    state={
        estado:""
    }


    componentDidMount = () => {
        this.situacionPartida();
         
     }


    situacionPartida= () =>{
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080" + this.props.location.pathname;
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
        .then( response =>{
                if(response.data==="jugador"){
                    this.setState({estado:"jugador"})
                }else{
                    this.setState({estado:"espectador"})
                }
                

                })
        }
    

    render(){
    return(
        <React.Fragment>
        <NavBar></NavBar>
        <div>
            <h1>Partida como {this.state.estado}</h1>
        </div>
        </React.Fragment>
        )

    }
}

export default function Redirect(props){
    const location = useLocation()
  
    return <Game {... props} location={location}/>;

}