import React, { useState } from 'react';


import './Login.css'; 

import logo from "../../logo.svg";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

import 'bootstrap/dist/css/bootstrap.min.css';
import { envLoader } from '../../env/envLoader';

const apiUrl = "http://localhost:8080/api";

function Login() {


    const [form,setForm] = useState({
      "username":"",
      "password":""
    })

    const [error,setError] = useState(false);

    const [errorMsg,setErrorMsg] = useState(false);


    const handleSubmit = (e) => {
      e.preventDefault();
    }


    const handleChange = async e =>{
      setForm({...form, [e.target.name]: e.target.value});

    }

    const handleButton = () => {
      let url = apiUrl + "/login";
      if (validate(form)){
        setError(false);
        axios.post(url,form)
        .then( response =>{
          
          if(response.status === 200){
            
            localStorage.setItem("jwtToken",response.data);
            window.location.replace('/games/list');
            
          }
        
        }).catch(error => {
          setError(true);
          setErrorMsg("Credenciales Incorrectas");
        });

      }else{
        setError(true);
        setErrorMsg("La longitud de los campos debe ser mayor que 1");
      }
      
    }
      
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

                <form onSubmit={handleSubmit}>
                  <input type="text"  className="fadeIn second" name="username" placeholder="Usuario" required onChange={handleChange}/>
                  <input type="password" className="fadeIn third" name="password" placeholder="Contraseña" required onChange={handleChange}/>
                  <input type="submit" className="fadeIn fourth" value="Iniciar Sesion" onClick={handleButton}/>
                </form>

            {error === true &&
              <div className="alert alert-danger" role = "alert">
                {errorMsg}
              </div>
            }
            </div>
            </div>
      
      </React.Fragment>



    )
}


function validate(props){
  if (props["username"].length > 1 && props["password"].length > 1){
    return true;
    }else{
      return false;
    }
  }


export default Login;

