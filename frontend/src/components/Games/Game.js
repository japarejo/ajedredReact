import React, { useEffect, useState } from 'react';

import axios from 'axios';


import { useLocation} from "react-router-dom"

import NavBar from '../../Navbar';

import Cookies from 'js-cookie';

import {envLoader} from '../../env/envLoader';



import './Game.css'; 

const apiUrl = "http://localhost:8080/api";



function Game() {

    const [pieces,setPieces] = useState([]);

    const[color,setColor] = useState();

    const[turn,setTurn] = useState();

    const[jaque,setJaque] = useState();

    const[time,setTime] = useState();

    const[timeOpponent, setTimeOpponent] = useState();

    const[myTurn, setMyTurn] = useState(false);

    const[coronacion,setCoronacion] = useState();

    const[finPartida, setFinPartida] = useState(false);


    const [inicializado,setInicializado] = useState("false");



    const[movimientos,setMovimientos] = useState([]);

    const [form,setForm] = useState({
        id: "0",
        xposition:"-1",
        yposition:"-1",
        type:"QUEEN" // Se utiliza para indicar la pieza que vamos a cambiar por el peon en la coronacion
    })

    const sampleLocation = useLocation();

    const [socket, setSocket] = useState(null);


    const DrawBoard = () => {
        
        var canvas = document.getElementById("canvas");
        var ctx = canvas.getContext("2d");
        var image;
       
        image = document.getElementById("source");

    
        ctx.drawImage(image,0,0,800,800);




        if(form.id !=0 && myTurn && !finPartida){
            movimientos.map(movimiento =>{
        
                ctx.fillStyle = '#00BFFF40';

                if(color === "BLACK"){
                    ctx.fillRect(700-(movimiento[0]*100), 700-(movimiento[1]*100),100,100);
                }else{
                    ctx.fillRect(movimiento[0]*100, movimiento[1]*100,100,100);
                }
                    
                })
        }

        

        pieces.map(piece =>{
            var pieza = document.getElementById(piece.type + "-" + piece.color);

            if(jaque && !finPartida && piece.color === turn && piece.type === "KING"){
                ctx.fillStyle = '#FF000040';
                if(color === "BLACK"){
                    ctx.fillRect(700-(piece.xposition*100), 700-(piece.yposition*100), 100, 100);
                }else{
                    ctx.fillRect(piece.xposition*100, piece.yposition*100, 100, 100);
                }
            }
            


            
            if(piece.id == form.id && piece.color === turn && !finPartida){
                ctx.fillStyle = '#00FF0040';
                if(color === "BLACK"){
                    ctx.fillRect(700-(piece.xposition*100), 700-(piece.yposition*100), 100, 100);
                }else{
                    ctx.fillRect(piece.xposition*100, piece.yposition*100, 100, 100);
                }
            }

            
            if(color === "BLACK"){
                ctx.drawImage(pieza,700-(piece.xposition*100),700-(piece.yposition*100),100,100);
            }else{
                ctx.drawImage(pieza,piece.xposition*100,piece.yposition*100,100,100);
            }
            
            
        })

    }




    const oMousePos = (evt) => {


        var canvas = document.getElementById("canvas");

        var ClientRect = canvas.getBoundingClientRect();

        var x = Math.floor(Math.round(evt.clientX - ClientRect.left)/100);
        var y = Math.floor(Math.round(evt.clientY - ClientRect.top)/100);

        const token = localStorage.getItem("jwtToken");
        
        //let url = apiUrl + "/games/listMovements/";

        if(!coronacion){

            setForm({id:"0",xposition:"-1",yposition:"-1"});


            pieces.map(piece =>{

                if(color==="BLACK" && myTurn && !coronacion){
                    if(7-piece.xposition === x && 7-piece.yposition === y && piece.color ===color){
                        setForm({id:piece.id,xposition:"-1",yposition:"-1"});
                        
                        let url = apiUrl + "/games/listMovements/" + piece.id; 

                        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
                        .then( response =>{
                                setMovimientos(response.data[0]);
                                setTime(response.data[1]);
                                Cookies.set('time',response.data[1]);
                                })
                    }
                
                }else if(myTurn && !coronacion){
                    if(piece.xposition === x && piece.yposition === y && piece.color ===color){
                        setForm({id:piece.id,xposition:"-1",yposition:"-1"});
                        
                        let url = apiUrl + "/games/listMovements/" + piece.id; 
                        
                        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
                        .then( response =>{
                                setMovimientos(response.data[0]);
                                setTime(response.data[1]);
                                Cookies.set('time',response.data[1]);
                                })
                    }
                }
                


            })


            if(form.id!=0){
                movimientos.map(movimiento =>{

                    if(color==="BLACK"){
                        if(7-movimiento[0]=== x && 7-movimiento[1]=== y){
                            setForm({...form,xposition:movimiento[0], yposition:movimiento[1]})
                            
        
                        }
                    }else{
                        if(movimiento[0]=== x && movimiento[1]=== y){
                            setForm({...form,xposition:movimiento[0], yposition:movimiento[1]})
        
                        }
                    }

                    
                })

            }

        }


    }



    async function InicioTurno()  {
        const token = localStorage.getItem("jwtToken");

        let url = apiUrl + sampleLocation.pathname + "/startTurn";
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}});
        

    }



    const finTiempo = () => {
        const token = localStorage.getItem("jwtToken");

        let url = apiUrl + sampleLocation.pathname + "/endTime";
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
        .then( response =>{

            setFinPartida(response.data[1]);
            setTime(response.data[2]);

            if(response.data[1] === true){
                Cookies.set("time", 0);

                setTurn(response.data[0].turn);
                setMyTurn(false);

                socket.send(color + "-" + response.data[2]);

                document.getElementById("msg").innerHTML = "¡¡Has perdido la partida por tiempo!!";

            } else if(response.data[2] > 0){
                Cookies.set("time",response.data[2]);
            }


        })
    }

    

    
    
    const partida = () => {
        const token = localStorage.getItem("jwtToken");

        let url = apiUrl + sampleLocation.pathname;
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
        .then( response =>{
            setJaque(response.data[0].jaque);
            setPieces(response.data[0].pieces);
            setTurn(response.data[0].turn);
            setCoronacion(response.data[0].coronacion);

            setColor(response.data[1]);

            if(response.data[0].coronacion === true && response.data[0].turn === response.data[1]){
                setForm({id:response.data[0].idCoronacion,xposition:"-1", yposition: "-1",type:"QUEEN"});
            }

            setTime(Cookies.get('time'));
            setFinPartida(response.data[2]);
            setTimeOpponent(Cookies.get('timeOpponent'));
            setInicializado("true");

            setMyTurn(response.data[0].turn === response.data[1]);



            if(response.data[0].jaqueMate === true && response.data[0].jaque === true && response.data[0].turn === response.data[1]){
                document.getElementById("msg").innerHTML = "¡¡Has perdido la partida!!";

            }else if(response.data[0].jaqueMate === true && response.data[0].jaque === true && response.data[0].turn !== response.data[1] && response.data[1] !== "espectador"){

                document.getElementById("msg").innerHTML = "¡¡Has ganado la partida!!";

            }else if(response.data[0].jaqueMate === true && response.data[0].jaque === false){
                document.getElementById("msg").innerHTML = "¡¡Tablas por rey ahogado!!";
            
            } else if(response.data[2] === true && response.data[0].turn === response.data[1]){
                setTimeOpponent(0);
                document.getElementById("msg").innerHTML = "¡¡Has ganado la partida por tiempo!!";
            
            }else if(response.data[2] === true && response.data[0].turn !== response.data[1] && response.data[1] !== "espectador"){
                document.getElementById("msg").innerHTML = "¡¡Has perdido la partida por tiempo!!";
            }else if(response.data[2] === true && response.data[1] === "espectador"){
                document.getElementById("msg").innerHTML = "¡¡Se acabó la partida!!";
            }
        })

    }





    
    const mover = () => {


        const token = localStorage.getItem("jwtToken");
        
        let url = apiUrl + sampleLocation.pathname + "/move";

        
            
        axios.post(url,form,{headers: {"Authorization": `Bearer  ${token}`}})
            .then(response =>{
                setTurn(response.data[0].turn);
                setJaque(response.data[0].jaque);
                setFinPartida(response.data[1]);

                if(response.data[0].coronacion === true){ // Si la coronacion es true, no cambio el turno hasta que seleccione una pieza para sustituir

                    setCoronacion(true);
                    setForm({...form,xposition:"-1", yposition: "-1",type:"QUEEN"});
                }else{
                    socket.send(color + "-" + response.data[2]);
                    setMyTurn(false);
                    setForm({id: "0",xposition:"-1",yposition:"-1"});
                }
                
                
                

                if(response.data[2]>=0){
                    Cookies.set("time",response.data[2]);
                }

                if(response.data[3]>=0){
                     Cookies.set("timeOpponent",response.data[3]);
                }

            })
        
    
    
    }
        




    useEffect(() => {

        
        const newSocket = new WebSocket('ws://localhost:8080' + sampleLocation.pathname + '/ws');


        partida();

        if(myTurn){
            InicioTurno();
        }


        if(!myTurn && color){
            newSocket.onmessage = (event) => {
                if(event.data !="Union" && color != "espectador" && event.data.split("-")[0] != color){
                    Cookies.set("timeOpponent", event.data.split("-")[1]);
                }
                partida();
            }

        }


        if(!myTurn && color){
            if(color == "espectador"){
                newSocket.onmessage = (event) => {
                    if(event.data !="Union" && event.data.split("-")[0] == "BLACK"){
                        Cookies.set("timeOpponent", event.data.split("-")[1]);
                    } else if(event.data !="Union" && event.data.split("-")[0] == "WHITE"){
                        Cookies.set("time", event.data.split("-")[1]);
                    }
                    partida();
                }
            }
            

        }

        setSocket(newSocket);

    
        if(form.xposition != "-1"){
                mover();
            }
        

        const interval = setInterval(() => {
            if ((myTurn || (color ==="espectador" && turn === "WHITE")) && !finPartida) {
                if(Cookies.get("time")>0){
                    setTime(time => time -1);
                    Cookies.set("time",Cookies.get("time")-1);
                }else if(Cookies.get("time") == 0){
                    finTiempo();
                }
            } else if(((!myTurn && color !="espectador") || (color =="espectador" && turn == "BLACK")) && !finPartida) {
                if(Cookies.get("timeOpponent")>0){
                    setTimeOpponent(timeOpponent => timeOpponent -1);
                    Cookies.set("timeOpponent",Cookies.get("timeOpponent")-1);
                }
                
            }
        }, 1000);
    

        return () => {
            clearInterval(interval);
          }



        
    },[form.id,form.xposition,turn,finPartida])


    

    const handleSubmit = (e) =>{
        e.preventDefault();
      }
  
  
     const handleChange = async e =>{
         setForm({...form,"type": e.target.value})
        
    }
        
  
  
      const handleButton =() => {
  
          const token = localStorage.getItem("jwtToken");
  
          let url = apiUrl + sampleLocation.pathname + "/move/coronacion";
        
         
          axios.post(url,form,{headers: {"Authorization": `Bearer  ${token}`}
  
          }).then( response =>{
              
            setTurn(response.data[0].turn);
                setJaque(response.data[0].jaque);
                setFinPartida(response.data[1]);
                setCoronacion(false);

                setMyTurn(false);
                setForm({id: "0"});

                socket.send(color + "-" + response.data[2]);
                
                if(response.data[2]>=0){
                    Cookies.set("time",response.data[2]);
                }

                if(response.data[3]>=0){
                     Cookies.set("timeOpponent",response.data[3]);
                }

  
  
          })
  
      }



    

 
    

    
    return(
        
        <React.Fragment>
        
        <NavBar></NavBar>

        {inicializado === "false" &&
            <div>
            
            <h1 style={{textAlign: 'center'}}>Cargando tablero... </h1>

            </div>
            
            }
        

        <div className="container">
            <br></br>

            <canvas id="canvas" width={800} height={800} onClick = {oMousePos}> </canvas>
            <img id="source" src={require('../../assets/img/tablero.png')} alt="alt" style={{display:'none'}}/>

            <img id="HORSE-BLACK" src={require('../../assets/img/HORSE-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="HORSE-WHITE" src={require('../../assets/img/HORSE-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="KING-BLACK" src={require('../../assets/img/KING-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="KING-WHITE" src={require('../../assets/img/KING-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="BISHOP-BLACK" src={require('../../assets/img/BISHOP-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="BISHOP-WHITE" src={require('../../assets/img/BISHOP-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="PAWN-BLACK" src={require('../../assets/img/PAWN-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="PAWN-WHITE" src={require('../../assets/img/PAWN-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="TOWER-WHITE" src={require('../../assets/img/TOWER-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="TOWER-BLACK" src={require('../../assets/img/TOWER-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="QUEEN-WHITE" src={require('../../assets/img/QUEEN-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="QUEEN-BLACK" src={require('../../assets/img/QUEEN-BLACK.png')} alt="alt" style={{display:'none'}}/>

            <h1 id = "msg"></h1>


            {inicializado === "true" &&
            <div>
                
                <DrawBoard /> 

            

            {myTurn && !finPartida &&

                <div>
                <h1 className="timeTurn"> {Math.floor(time/60)}:{time%60 < 10? '0' + time%60: time%60}</h1>

                <h1 className="timeOpponent"> {Math.floor(timeOpponent/60)}:{timeOpponent%60 < 10? '0' + timeOpponent%60: timeOpponent%60}</h1>

                </div>

            }

            {!myTurn && color !== "espectador" && !finPartida &&

                <div>
                <h1 className="timeTurnOpponent"> {Math.floor(time/60)}:{time%60 < 10? '0' + time%60: time%60}</h1>

                <h1 className="timeOpponentTurn"> {Math.floor(timeOpponent/60)}:{timeOpponent%60 < 10? '0' + timeOpponent%60: timeOpponent%60}</h1>

                </div>

            } {color ==="espectador" && turn === "WHITE" && !finPartida &&

                <div>
                <h1 className="timeTurn"> {Math.floor(time/60)}:{time%60 < 10? '0' + time%60: time%60}</h1>

                <h1 className="timeOpponent"> {Math.floor(timeOpponent/60)}:{timeOpponent%60 < 10? '0' + timeOpponent%60: timeOpponent%60}</h1>

                </div>

            }{color ==="espectador" && turn === "BLACK" && !finPartida &&

                <div>
                <h1 className="timeTurnOpponent"> {Math.floor(time/60)}:{time%60 < 10? '0' + time%60: time%60}</h1>

                <h1 className="timeOpponentTurn"> {Math.floor(timeOpponent/60)}:{timeOpponent%60 < 10? '0' + timeOpponent%60: timeOpponent%60}</h1>

                </div>


            }


            {finPartida &&

                <div>
                <h1 className="timeTurnOpponent"> {Math.floor(time/60)}:{time%60 < 10? '0' + time%60: time%60}</h1>

                <h1 className="timeOpponent"> {Math.floor(timeOpponent/60)}:{timeOpponent%60 < 10? '0' + timeOpponent%60: timeOpponent%60}</h1>

                </div>

            }


            { myTurn && coronacion &&
            
                <div>
                <form className="form" onSubmit={handleSubmit}>
                  <label htmlFor="myInput" className='centered-label'>
                        Escoja una pieza:       
                        <select name="type" onChange={handleChange}>
                        
                        <option value="QUEEN">Reina</option>
                        <option value="BISHOP">Alfil</option>
                        <option value="TOWER">Torre</option>
                        <option value="HORSE">Caballo</option>
                        </select>
                </label>
                <br></br>

                  <input className = "my-button" type="button" value="Cambiar pieza" onClick={handleButton}/>
                </form>

                </div>
            
            
            }

            </div>
            }




            

            </div>
            



        
        </React.Fragment>
        
        


        
        )

    
}

export default Game;