import React, { useEffect, useState } from 'react';

import axios from 'axios';

import { useLocation} from "react-router-dom"

import NavBar from '../../Navbar';


import './Game.css'; 


function Game() {

    const [pieces,setPieces] = useState([]);

    const[color,setColor] = useState();

    const[turn,setTurn] = useState();

    const[jaque,setJaque] = useState();

    const[time,setTime] = useState();

    const[timeOpponent, setTimeOpponent] = useState();

    const[myTurn, setMyTurn] = useState(false);

    const[finPartida, setFinPartida] = useState(false);


    const [inicializado,setInicializado] = useState("false");



    const[movimientos,setMovimientos] = useState([]);

    const [form,setForm] = useState({
        id: "0",
        xposition:"-1",
        yposition:"-1"
    })


    



    const sampleLocation = useLocation();





    


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

            if(jaque && !finPartida){
                if(piece.type === "KING" && piece.color === turn){
                    ctx.fillStyle = '#FF000040';
                    if(color === "BLACK"){
                        ctx.fillRect(700-(piece.xposition*100), 700-(piece.yposition*100), 100, 100);
                    }else{
                        ctx.fillRect(piece.xposition*100, piece.yposition*100, 100, 100);
                    }
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


        if(myTurn){
            window.addEventListener("click",oMousePos);
        }

    }



    const oMousePos = (evt) => {
        var canvas = document.getElementById("canvas");

        var ClientRect = canvas.getBoundingClientRect();

        var x = Math.floor(Math.round(evt.clientX - ClientRect.left)/100);
        var y = Math.floor(Math.round(evt.clientY - ClientRect.top)/100);

        if(form.id!=0){
            movimientos.map(movimiento =>{

                if(color==="BLACK"){
                    if(7-movimiento[0]=== x && 7-movimiento[1]=== y){
                        setForm({...form,xposition:movimiento[0], yposition:movimiento[1]})
                        window.removeEventListener("click",oMousePos);
      
                    }
                }else{
                    if(movimiento[0]=== x && movimiento[1]=== y){
                        setForm({...form,xposition:movimiento[0], yposition:movimiento[1]})
                        window.removeEventListener("click",oMousePos);
      
                    }
                }

                
            })

        }

        pieces.map(piece =>{

            if(color==="BLACK"){
                if(7-piece.xposition === x && 7-piece.yposition === y && piece.color ===color){
                    setForm({id:piece.id,xposition:"-1",yposition:"-1"});
                    window.removeEventListener("click",oMousePos);
                }
            
            }else{
                if(piece.xposition === x && piece.yposition === y && piece.color ===color){
                    setForm({id:piece.id,xposition:"-1",yposition:"-1"});
                    window.removeEventListener("click",oMousePos);
                }
            }
            


        })


        


       }




    async function InicioTurno()  {
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080" + sampleLocation.pathname + "/startTurn";
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}});
        

    }



    const finTiempo = () => {
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080" + sampleLocation.pathname + "/endTime";
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
        .then( response =>{

            setFinPartida(response.data[1]);

            setTime(response.data[2]);

            console.log(response.data[2]);

            if(response.data[1] === true){
                localStorage.setItem("time", 0);

                setTurn(response.data[0].turn);

                console.log(response.data[0].turn);
                setMyTurn(false);

                document.getElementById("msg").innerHTML = "¡¡Has perdido la partida por tiempo!!";

            } else if(response.data[2] > 0){
                localStorage.setItem("time",response.data[2]);
            }


        })
    }

    

    
    
    const partida = () => {
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080" + sampleLocation.pathname;
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
        .then( response =>{
            console.log(response.data[0].turn);
            //setTime(localStorage.getItem("time"));
            setPieces(response.data[0].pieces);
            setTurn(response.data[0].turn);
            setJaque(response.data[0].jaque);
            setColor(response.data[1]);
            setTime(localStorage.getItem("time"));
            setFinPartida(response.data[3]);
            setTimeOpponent(response.data[4]);
            setInicializado("true");

            setMyTurn(response.data[0].turn === response.data[1]);

            if(response.data[0].jaqueMate === true && response.data[0].turn === response.data[1]){
                document.getElementById("msg").innerHTML = "¡¡Has perdido la partida!!";

            }else if(response.data[0].jaqueMate === true && response.data[0].turn !== response.data[1]){

                document.getElementById("msg").innerHTML = "¡¡Has ganado la partida!!";
            
            } else if(response.data[3] === true && response.data[0].turn === response.data[1]){
                document.getElementById("msg").innerHTML = "¡¡Has ganado la partida por tiempo!!";
            
            }else if(response.data[3] === true && response.data[0].turn !== response.data[1]){
                document.getElementById("msg").innerHTML = "¡¡Has perdido la partida por tiempo!!";
            }
            })

    }




    const refresco = () => {
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080" + sampleLocation.pathname;
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
        .then( response =>{
            setPieces(response.data[0].pieces);
            setTurn(response.data[0].turn);
            setMyTurn(response.data[0].turn === response.data[1]);
            })

    }


    const listaMovimientos = () =>{

        const token = localStorage.getItem("jwtToken");
        
        let url = "http://localhost:8080/games/listMovements";
        axios.post(url,form,{ headers: { "Authorization": `Bearer  ${token}`}})
            .then( response =>{
                setMovimientos(response.data);
                    })
            }


    
    const mover = () => {


        const token = localStorage.getItem("jwtToken");
        
        let url = "http://localhost:8080" + sampleLocation.pathname + "/move";

        
            
        axios.post(url,form,{headers: {"Authorization": `Bearer  ${token}`}})
            .then(response =>{
                setTurn(response.data[0].turn);
                setMyTurn(false);
                setFinPartida(response.data[1]);
                //setTime(response.data[2]);

                if(response.data[2]>=0){
                    localStorage.setItem("time",response.data[2]);
                }

            })
        
    
    
    }

    

       


    
        


    useEffect(() => {
        partida();

        if(myTurn){
            
            InicioTurno();
        }
            
        const descontarTiempo = setInterval(() => {
            if (myTurn && !finPartida) {
                if(localStorage.getItem("time")>0){
                    setTime(time => time -1);
                    localStorage.setItem("time",localStorage.getItem("time")-1);
                }else if(localStorage.getItem("time") == 0){
                    finTiempo();
                }
            }

        },1000)

        const interval = setInterval(() => {
            if (!myTurn && !finPartida) {
                refresco();
            }

        },1000)
        

        if(form.id!=0){
            listaMovimientos();

            if(form.xposition != "-1"){
                mover();
            }
        }


        

        return () => {
            setForm({id:"0"});
            clearInterval(interval);
            clearInterval(descontarTiempo);
        }

        
    },[form.id,form.xposition,myTurn,finPartida])






    

 
    

    
    return(
        
        <React.Fragment>
        
        <NavBar></NavBar>
        <div className="container">
            <br></br>

            <canvas id="canvas" width={800} height={800}> </canvas>
            <img id="source" src={require('../../assets/img/tablero.png')} alt="alt" style={{display:'none'}}/>

            <img id="HORSE-BLACK" src={require('../../assets/img/HORSE-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="KING-BLACK" src={require('../../assets/img/KING-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="KING-WHITE" src={require('../../assets/img/KING-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="BISHOP-BLACK" src={require('../../assets/img/BISHOP-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="PAWN-BLACK" src={require('../../assets/img/PAWN-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="PAWN-WHITE" src={require('../../assets/img/PAWN-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="TOWER-WHITE" src={require('../../assets/img/TOWER-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="TOWER-BLACK" src={require('../../assets/img/TOWER-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="QUEEN-WHITE" src={require('../../assets/img/QUEEN-WHITE.png')} alt="alt" style={{display:'none'}}/>

            <h1 id="msg"></h1>

            {inicializado === "true" &&
            <div>
                
                <DrawBoard /> 


            {myTurn && !finPartida &&

                <div>
                <h1 className="timeTurn"> {Math.floor(time/60)}:{time%60 < 10? '0' + time%60: time%60}</h1>

                <h1 className="timeOpponent"> {Math.floor(timeOpponent/60)}:{timeOpponent%60 < 10? '0' + timeOpponent%60: timeOpponent%60}</h1>

                </div>

            }

            {!myTurn && !finPartida &&

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

            </div>
            }

            

            </div>
            



        
        </React.Fragment>
        
        


        
        )

    
}

export default Game;