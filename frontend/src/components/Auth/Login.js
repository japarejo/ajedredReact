import React from 'react';


import './Login.css'; 

import logo from "../../logo.svg";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

import 'bootstrap/dist/css/bootstrap.min.css';
import { envLoader } from '../../env/envLoader';

const apiUrl = "https://ajedrezreact.ey.r.appspot.com/api";

class Login extends React.Component{


    state={
      form:{
        "username":"",
        "password":""
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
      let url = apiUrl + "/login";
      if (validate(this.state.form)){
        this.setState({
          error:false,
        })
        axios.post(url,this.state.form)
        .then( response =>{
          
          if(response.status === 200){
            
            localStorage.setItem("jwtToken",response.data);
            window.location.replace('/games/list');
            
          }
        
        }).catch(error => {
          this.setState({
            error:true,
            errorMsg : "Credenciales Incorrectas"
          })
        });

      }
      
    }



    render(){
      return(
        
       <React.Fragment>
          <br></br>
          <br></br>
          <br></br>
          <br></br>
            <div className="wrapper fadeInDown">
              <div id="formContent">
              
                <div className="fadeIn first">
                  <img src={logo} width="100px" alt="User Icon" />
                </div>

                <form onSubmit={this.handleSubmit}>
                  <input type="text"  className="fadeIn second" name="username" placeholder="Usuario" required onChange={this.handleChange}/>
                  <input type="password" className="fadeIn third" name="password" placeholder="Contraseña" required onChange={this.handleChange}/>
                  <input type="submit" className="fadeIn fourth" value="Iniciar Sesion" onClick={this.handleButton}/>
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

  return <Login {... props} navigate={navigate}/>;
}


function validate(props){
  if (props["username"].length > 1 && props["password"].length > 1){
    return true;
    }else{
      return false;
    }
  }

