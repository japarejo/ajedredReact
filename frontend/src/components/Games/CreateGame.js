import React from 'react';


import './CreateGame.css'; 

import logo from "../../logo.svg";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

import NavBar from '../../Navbar';


class CreateGame extends React.Component{


    state={
      form:{
        "name":"",
        "tiempo":"3",
        "espectadores":"True",
      },
      error : false,
      errorMsg:""
    }


    handleSubmit(e){
      e.preventDefault();
    }


    handleChange = async e =>{
       this.setState({
        form:{
          ...this.state.form,
          [e.target.name]: e.target.value
        }
      })

    }

    handleButton =() => {

        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080/games/create";
      
       
        axios.post(url,this.state.form,
        {
            headers: {
                "Authorization": `Bearer  ${token}`
            }

        }).then( response =>{
            if(response.status===200){
                const id = response.data;
                this.props.navigate("/games/"+ id + "/awaitGame");

            }


        })

    }



    render(){
      return(
        
       <React.Fragment>
        <NavBar></NavBar>
            
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossOrigin="anonymous"/>
            

            <div className="wrapper fadeInDown">
              <div id="formContent">
              
                <div className="fadeIn first">
                  <img src={logo} width="100px" alt="User Icon" />
                </div>

                <form onSubmit={this.handleSubmit}>
                  <input type="text"  className="fadeIn second" name="name" placeholder="Nombre" required onChange={this.handleChange}/>
                  <label>
                        Tiempo Movimiento:       
                        <select name="tiempo" value={this.state.tiempo} onChange={this.handleChange}>
                        
                        <option value="3">3 min</option>
                        <option value="5">5 min</option>
                        <option value="10">10 min</option>
                        </select>
                </label>
                <br></br>
                <br></br>

                <label>
                        Espectadores:       
                        <select name="espectadores" value={this.state.espectadores} onChange={this.handleChange}>
                        
                        <option value="True">Si</option>
                        <option value="False">No</option>
                        </select>
                </label>

                <br></br>
                <br></br>


                  <input type="submit" className="fadeIn fourth" value="Crear Partida" onClick={this.handleButton}/>
                </form>

            {this.state.error === true &&
              <div className="alert alert-danger" role = "alert">
                {this.state.errorMsg}
              </div>
            }
            </div>
            </div>
      
      </React.Fragment>



    )
  }
}

export default function Redirect(props){
  const navigate = useNavigate();

  return <CreateGame {... props} navigate={navigate}/>;
}
