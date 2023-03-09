import React, { useEffect, useState } from 'react';

import axios from 'axios';

import { useLocation} from "react-router-dom"

import NavBar from '../../Navbar';





function Game() {

    const [pieces,setPieces] = useState([]);

    const[color,setColor] = useState();

    const[turn,setTurn] = useState();

    const[jaque,setJaque] = useState();

    const[time,setTime] = useState();

    const[myTurn, setMyTurn] = useState(false);

    const[finPartida, setFinPartida] = useState();


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




        if(form.id !=0){
            movimientos.map(movimiento =>{
        
                ctx.fillStyle = '#b0c4de';

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
                    ctx.fillStyle = '#52A77E';
                    if(color === "BLACK"){
                        ctx.fillRect(700-(piece.xposition*100), 700-(piece.yposition*100), 100, 100);
                    }else{
                        ctx.fillRect(piece.xposition*100, piece.yposition*100, 100, 100);
                    }
                }
            }


            
            if(piece.id == form.id){
                ctx.fillStyle = '#FF0000';
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

        if(color === turn && !finPartida){
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




    function InicioTurno ()  {
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080" + sampleLocation.pathname + "/startTurn";
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}});

        

        

    }

    

    
    
    const tablero = () => {
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080" + sampleLocation.pathname;
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
        .then( response =>{
            console.log(response.data[0].jaque);
            setPieces(response.data[0].pieces);
            setTurn(response.data[0].turn);
            setJaque(response.data[0].jaque);
            setColor(response.data[1]);
            setTime(response.data[2]);
            setFinPartida(response.data[3]);
            setInicializado("true");

            setMyTurn(response.data[0].turn === response.data[1]);

            if(response.data[3] === true){
                alert("Has perdido la partida");
                window.location.href(sampleLocation.pathname);
            }
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


    
    const handleButton = () => {


        const token = localStorage.getItem("jwtToken");
        
        let url = "http://localhost:8080" + sampleLocation.pathname + "/move";

        
            
        axios.post(url,form,{headers: {"Authorization": `Bearer  ${token}`}})
            .then(response =>{
                setMyTurn(false);
                //setPieces(response.data[0].pieces);
                setTurn(response.data[0].turn);

                setFinPartida(response.data[1]);

                if(response.data[1] === true){
                    alert("Has ganado la partida");
                    window.location.replace(sampleLocation.pathname);
                }

            })
        
    
    
    }

    

       


    
        


    useEffect(() => {
        tablero();

        const interval = setInterval(() => {
            if (!myTurn && !finPartida) {
                tablero();
            }

        },1000)
        

        if(form.id!=0){
            listaMovimientos();

            if(form.xposition != "-1"){
                handleButton();
            }
        }


        

        return () => {
            setForm({id:"0"});
            clearInterval(interval);
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

            {inicializado === "true" &&
            <div>
                
                <DrawBoard /> 

                {turn === color &&

            <div>

                <InicioTurno />
            </div>
                }

            <h1>{Math.floor(time/60)}:{time%60 < 10? '0' + time%60: time%60}</h1>
            </div>
            }

            

            </div>
            



        
        </React.Fragment>
        
        


        
        )

    
}

export default Game;