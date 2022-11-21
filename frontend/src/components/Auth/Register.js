import React from 'react';

import './Login.css';

import logo from "../../logo.svg";

import axios from 'axios';


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
              window.location.reload('');
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

            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossOrigin="anonymous"/>

              <div className="wrapper fadeInDown">
              <div id="formContent">
              
                <div className="fadeIn first">
                  <img src={logo} width="100px" alt="User Icon" />
                </div>

                <form onSubmit={this.handleSubmit}>
                  <input type="text"  className="fadeIn second" name="firstName" placeholder="First Name" required onChange={this.handleChange}/>
                  <input type="text"  className="fadeIn second" name="lastName" placeholder="Last Name" required onChange={this.handleChange}/>
                  <input type="text"  className="fadeIn second" name="telephone" placeholder="Telephone" required onChange={this.handleChange}/>
                  <input type="text"  className="fadeIn second" name="username" placeholder="Username" required onChange={this.handleChange}/>
                  <input type="password" className="fadeIn third" name="password" placeholder="Password" required onChange={this.handleChange}/>
                  <input type="submit" className="fadeIn fourth" value="Register" onClick={this.handleButton}/>
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






