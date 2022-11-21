import React from 'react';


import './Login.css'; 

import logo from "../../logo.svg";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

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
      let url = "http://localhost:8080/login";
      if (validate(this.state.form)){

        axios.post(url,this.state.form)
        .then( response =>{
          console.log(response);
          if(response.status === 200){
            
            localStorage.setItem("jwtToken",response.data.jwtToken);
            this.props.navigate('/dashboard');
            
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
            
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossOrigin="anonymous"/>
            

            <div className="wrapper fadeInDown">
              <div id="formContent">
              
                <div className="fadeIn first">
                  <img src={logo} width="100px" alt="User Icon" />
                </div>

                <form onSubmit={this.handleSubmit}>
                  <input type="text"  className="fadeIn second" name="username" placeholder="Usuario" required onChange={this.handleChange}/>
                  <input type="password" className="fadeIn third" name="password" placeholder="Password" required onChange={this.handleChange}/>
                  <input type="submit" className="fadeIn fourth" value="Log In" onClick={this.handleButton}/>
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

