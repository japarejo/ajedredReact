import React from 'react';


import './Login.css'; 

import logo from "../../logo.svg";
import axios from 'axios';

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
      await this.setState({
        form:{
          ...this.state.form,
          [e.target.name]: e.target.value
        }
      })

    }

    handleButton =() => {
      let url = "/login";
      axios.post(url,this.state.form)
      .then( response =>{
        if(response.statusText === "OK"){
          localStorage.setItem("jwtToken",response.data.jwtToken);
          window.location.replace('/dashboard');
          
        
        }else{
          this.setState({
            error:true,
            errorMsg : "Credenciales Incorrectas"
          })
        }
      
      }).catch(error => {
        this.setState({
          error:true,
          errorMsg : "Credenciales Incorrectas"
        })
      });
      
    }



    render(){
      return(
       <React.Fragment>

            <div className="wrapper fadeInDown">
              <div id="formContent">
              
                <div className="fadeIn first">
                  <img src={logo} width="100px" alt="User Icon" />
                </div>

                <form onSubmit={this.handleSubmit}>
                  <input type="text"  className="fadeIn second" name="username" placeholder="Usuario" required="required" onChange={this.handleChange}/>
                  <input type="password" className="fadeIn third" name="password" placeholder="Password" required="required" onChange={this.handleChange}/>
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

export default Login;