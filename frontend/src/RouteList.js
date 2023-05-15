import {Routes,Route,BrowserRouter,Navigate} from "react-router-dom";
import Welcome from "./Welcome";
import Login from "./components/Auth/Login";
import Register from "./components/Auth/Register";
import CreateGame from "./components/Games/CreateGame";
import AwaitGame from "./components/Games/AwaitGame";
import Game from "./components/Games/Game";
import ListGame from "./components/Games/ListGame";
import EditGame from "./components/Player/EditPlayer";

import axios from 'axios';

import React, { useState, useEffect } from 'react';

const apiUrl = "http://localhost:8080/api";

export const RouteList = () => {
    
    const esTokenValido = async (token) =>{
          
        try { 

            let url = apiUrl + "/auth/validate_token/" + token;
            const response = await axios.get(url);
            if(response.data === 200){
                return true;
            }else{
                return false;
            }
        } catch(error) {
            console.log("Error al validar token:", error);
            return false;
        }
    }


    const HandleAuth = ({component: Component}) => {
        const [esValido, setEsValido] = useState(null);
      
        useEffect(() => {
          const validar = async () => {
            const token = localStorage.getItem('jwtToken');
            const resultado = await esTokenValido(token);
            setEsValido(resultado);
          };
          validar();
        }, []);
      
        if (esValido === null) {

        } else if (esValido) {
          return <Component/>;
        } else {
          return <Navigate to="/login" replace />;
        }
      };


    const HandleInit = ({component: Component}) => {
        const [esValido, setEsValido] = useState(null);
      
        useEffect(() => {
          const validar = async () => {
            const token = localStorage.getItem('jwtToken');
            const resultado = await esTokenValido(token);
            setEsValido(resultado);
          };
          validar();
        }, []);
      
        if (esValido === null) {

        } else if (esValido) {
          return <Navigate to="/games/list" replace />;
        } else {
          return <Component/>;
        }
      };




      const LogOut = () => {
      
        useEffect(() => {
          const eliminarToken = async () => {
            localStorage.removeItem('jwtToken')
          };
          
          eliminarToken();
        }, []);
      
        return <Navigate to="/" replace />;
      };



      








    
    return (
        <BrowserRouter>
        <Routes>
            <Route path = '/' element = {<HandleInit component={Welcome} />} />
            <Route path = '/login' element = {<HandleInit component={Login} />} />
            <Route path = '/register' element ={<HandleInit component={Register} />} />

            <Route path = '/games/list' element={<HandleAuth component={ListGame} />}/>
            <Route path = '/games/create' element = {<HandleAuth component={CreateGame} />} />
            <Route path = '/games/:id/awaitGame' element = {<HandleAuth component={AwaitGame} />} />
            <Route path = '/games/:id' element = {<HandleAuth component={Game} />} />

            <Route path = '/player' element = {<HandleAuth component={EditGame} />} />

            <Route path = '/logout' element = {<LogOut/>} />

            <Route path="*" element={<Navigate to="/login" replace />} />
            
        </Routes>
        </BrowserRouter>
    )
}