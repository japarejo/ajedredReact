import React from 'react';


import './CreateGame.css'; 

import logo from "../../logo.svg";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

import { envLoader } from '../../env/envLoader';

import Cookies from 'js-cookie';


import NavBar from '../../Navbar';

const apiUrl = "http://localhost:8080/api";


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

        let url = apiUrl + "/games/create";

        this.setState({
          error:false,
        })
      
       
        axios.post(url,this.state.form,
        {
            headers: {
                "Authorization": `Bearer  ${token}`
            }

        }).then( response =>{
            if(response.status===200){
                const id = response.data;

                Cookies.set("time",this.state.form.tiempo * 60);
                Cookies.set("timeOpponent",this.state.form.tiempo * 60);
                this.props.navigate("/games/"+ id + "/awaitGame");

            }


        }).catch(error => {
          this.setState({
            error:true,
            errorMsg : "Ese nombre de partida ya ha sido utilizado"
        })

      });

    }



    render(){
      return(
        
       <React.Fragment>
        <NavBar></NavBar>            

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
