import React from 'react';

import './Login.css';

import logo from "../../logo.svg";

import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.min.css';



class Register extends React.Component{



    state={
        form:{
        "firstName":"",
        "lastName": "",
        "telephone": "",
        "user":{"username":"", "password":""}
        },
        error : false,
        errorMsg:""
      }


      handleSubmit(e){
        e.preventDefault();
      }
  
  
      handleChange = async e =>{

        if(e.target.name==='username' || e.target.name==='password'){
            this.setState({
            form:{
                ...this.state.form,
                user:{...this.state.form.user,[e.target.name]: e.target.value}
                }
            })
      
        } else {
            this.setState({
                form:{
                    ...this.state.form,
                    [e.target.name]: e.target.value
                    }
                })
      }
  
      }


     
  
      handleButton =(e) => {

        let url = "http://localhost:8080/register";

        if(validate(this.state.form)){
          axios.post(url,this.state.form)
          .then( response =>{
            if(response.data==='OK'){
              alert("Se ha registrado el usuario correctamente");
              window.location.replace('/login');
            }else{
              this.setState({
                error:true,
                errorMsg : "El nombre de usuario ya existe"
              })
            }

        })
        
        }
       
        
        
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
                  <input type="text"  className="fadeIn second" name="firstName" placeholder="Nombre" required onChange={this.handleChange}/>
                  <input type="text"  className="fadeIn second" name="lastName" placeholder="Apellidos" required onChange={this.handleChange}/>
                  <input type="text"  className="fadeIn second" name="telephone" placeholder="Telefono" required onChange={this.handleChange}/>
                  <input type="text"  className="fadeIn second" name="username" placeholder="Usuario" required onChange={this.handleChange}/>
                  <input type="password" className="fadeIn third" name="password" placeholder="Contraseña" required onChange={this.handleChange}/>
                  <input type="submit" className="fadeIn fourth" value="Registrarse" onClick={this.handleButton}/>
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

export default Register;

function validate(props){
  if (props["firstName"].length > 1 && props["lastName"].length > 1 && props["telephone"].length > 1 && props.user["username"].length > 1 && props.user["password"].length > 1){
    return true;
    }else{
      return false;
    }
  }






