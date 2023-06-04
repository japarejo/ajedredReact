import React,{useState} from 'react';


import './CreateGame.css'; 

import logo from "../../logo.svg";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

import { envLoader } from '../../env/envLoader';

import Cookies from 'js-cookie';


import NavBar from '../../Navbar';

const apiUrl = "http://localhost:8080/api";


function CreateGame() {

  const [form,setForm] = useState({
    "name":"",
    "tiempo":"3",
    "espectadores":"True",
  })

  const [error,setError] = useState(false);

  const [errorMsg,setErrorMsg] = useState("");


    const handleSubmit = (e) => {
      e.preventDefault();
    }


    const handleChange = async e =>{
       setForm({...form,[e.target.name]: e.target.value});
      

    }

    const handleButton = () => {

        const token = localStorage.getItem("jwtToken");

        let url = apiUrl + "/games/create";

        setError(false);
      
       
        axios.post(url,form,
        {
            headers: {
                "Authorization": `Bearer  ${token}`
            }

        }).then( response =>{
            if(response.status===200){
                const id = response.data;

                Cookies.set("time",form.tiempo * 60);
                Cookies.set("timeOpponent",form.tiempo * 60);
                window.location.replace("/games/"+ id + "/awaitGame");

            }


        }).catch(error => {
          setError(true);
          setErrorMsg("Ese nombre de partida ya ha sido utilizado");
        
        });

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
                  <input type="text"  className="fadeIn second" name="name" placeholder="Nombre" required onChange={handleChange}/>
                  <label>
                        Tiempo Movimiento:       
                        <select name="tiempo" value={form.tiempo} onChange={handleChange}>
                        
                        <option value="3">3 min</option>
                        <option value="5">5 min</option>
                        <option value="10">10 min</option>
                        </select>
                </label>
                <br></br>
                <br></br>

                <label>
                        Espectadores:       
                        <select name="espectadores" value={form.espectadores} onChange={handleChange}>
                        
                        <option value="True">Si</option>
                        <option value="False">No</option>
                        </select>
                </label>

                <br></br>
                <br></br>


                  <input type="submit" className="fadeIn fourth" value="Crear Partida" onClick={handleButton}/>
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

export default CreateGame;

