import React, { useEffect,useState } from 'react';

import axios from 'axios';

import './EditPlayer.css';

import logo from "../../logo.svg";

import {envLoader} from '../../env/envLoader';


import NavBar from '../../Navbar';

const apiUrl = "http://localhost:8080/api";

function EditPlayer() {


    const [form,setForm] = useState({
      "firstName":"",
      "lastName": "",
      "telephone": "",
      "user":{"username":"", "password":""}
    })

    const [error,setError] = useState(false);

    const [errorMsg,setErrorMsg] = useState(false);


    useEffect(() =>{
       player();
        
    },[]);


    const player = () =>{
        const token = localStorage.getItem("jwtToken");

        let url =  apiUrl + "/player/data";
      
       
        axios.get(url,
            {
                headers: {
                "Authorization": `Bearer  ${token}`
                }

            }).then( response =>{
                let player = response.data;

                setForm({"firstName": player.firstName,"lastName" : player.lastName,"telephone": player.telephone,"user":{"username": player.user.username,"password": player.user.password}});
                })

    }


    const handleSubmit =(e) =>{
        e.preventDefault();
      }
  
  
    const handleChange = async e =>{

        if(e.target.name==='username' || e.target.name==='password'){
            setForm({...form,user:{...form.user,[e.target.name]: e.target.value}})
      
        } else {
            setForm({...form, [e.target.name] : e.target.value});
      }
  
      }


     
  
    const handleButton =(e) => {

        const token = localStorage.getItem("jwtToken");

        let url = apiUrl + "/player/update";

        if(validate(form)){
          axios.post(url,form,
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
              
            }

        }).catch(error => {
          setError(true);
          setErrorMsg("El nombre de usuario ya existe");
        });
        
        }else{
          setError(true);
          setErrorMsg("La longitud de los campos debe ser mayor que 1");
        }
       
        
        
    }

        return(

            <React.Fragment>

            <NavBar></NavBar>

              <div className="wrapper fadeInDown">
              <div id="formContent">
              
                <div className="fadeIn first">
                  <img src={logo} width="100px" alt="User Icon" />
                </div>

                <form onSubmit={handleSubmit}>
                  <input type="text"  className="fadeIn second" name="firstName" placeholder="Nombre" value={form.firstName} required onChange={handleChange}/>
                  <input type="text"  className="fadeIn second" name="lastName" placeholder="Apellidos" value={form.lastName} required onChange={handleChange}/>
                  <input type="text"  className="fadeIn second" name="telephone" placeholder="Telefono" value={form.telephone} required onChange={handleChange}/>
                  <input type="text"  className="fadeIn second" name="username" placeholder="Usuario" value={form.user.username} required onChange={handleChange}/>
                  <input type="password" className="fadeIn third" name="password" placeholder="Contraseña" value={form.user.password} required onChange={handleChange}/>
                  <input type="submit"  value="Actualizar" onClick={handleButton}/>
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
  if (props["firstName"].length > 1 && props["lastName"].length > 1 && props["telephone"].length > 1){
    return true;
    }else{
      return false;
    }
  }

export default EditPlayer;