import React from 'react';

import axios from 'axios';

import './EditPlayer.css';

import logo from "../../logo.svg";

import NavBar from '../../Navbar';

class EditPlayer extends React.Component{



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

    componentDidMount = () => {
       this.player();
        
    }


    player = () =>{
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080/player/data";
      
       
        axios.get(url,
            {
                headers: {
                "Authorization": `Bearer  ${token}`
                }

            }).then( response =>{
                let player = response.data
                this.setState({
                    form:{
                        "firstName": player.firstName,
                        "lastName" : player.lastName,
                        "telephone": player.telephone,
                        "user":{"username": player.user.username,"password": player.user.password}
                        }
                    })
                })

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

        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080/player/update";

        if(validate(this.state.form)){
          axios.put(url,this.state.form,
            {
                headers: {
                    "Authorization": `Bearer  ${token}`
                }
    
            })
          .then( response =>{
            console.log(response)
            if(response.status===200){

              if(response.data.jwtToken){
                localStorage.setItem("jwtToken",response.data.jwtToken);
              }
              alert("Se han modificado los datos correctamente");
              window.location.replace("/games/list");
              
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

            <NavBar></NavBar>

              <div className="wrapper fadeInDown">
              <div id="formContent">
              
                <div className="fadeIn first">
                  <img src={logo} width="100px" alt="User Icon" />
                </div>

                <form onSubmit={this.handleSubmit}>
                  <input type="text"  className="fadeIn second" name="firstName" placeholder="Nombre" value={this.state.form.firstName} required onChange={this.handleChange}/>
                  <input type="text"  className="fadeIn second" name="lastName" placeholder="Apellidos" value={this.state.form.lastName} required onChange={this.handleChange}/>
                  <input type="text"  className="fadeIn second" name="telephone" placeholder="Telefono" value={this.state.form.telephone} required onChange={this.handleChange}/>
                  <input type="text"  className="fadeIn second" name="username" placeholder="Usuario" value={this.state.form.user.username} required onChange={this.handleChange}/>
                  <input type="password" className="fadeIn third" name="password" placeholder="Contraseña" value={this.state.form.user.password} required onChange={this.handleChange}/>
                  <input type="submit"  value="Actualizar" onClick={this.handleButton}/>
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

export default EditPlayer;

function validate(props){
  if (props["firstName"].length > 1 && props["lastName"].length > 1 && props["telephone"].length > 1){
    return true;
    }else{
      return false;
    }
  }