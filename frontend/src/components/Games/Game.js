import React, { useEffect, useState } from 'react';

import axios from 'axios';

import { useLocation} from "react-router-dom"

import NavBar from '../../Navbar';





function Game() {

    const [pieces,setPieces] = useState([]);

    const [done,setDone] = useState("false");

    const[reload,setReload] = useState("false");


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
                
                var ctx = canvas.getContext("2d");
        
                ctx.fillStyle = '#b0c4de';
                ctx.fillRect(movimiento[0]*100, movimiento[1]*100,100,100);
                    
                })
        }

        

        pieces.map(piece =>{
            var pieza = document.getElementById(piece.type + "-" + piece.color);
            var ctx = canvas.getContext("2d");


            
            if(piece.id == form.id){
                ctx.fillStyle = '#FF0000';
                ctx.fillRect(piece.xposition*100, piece.yposition*100, 100, 100);
            }
            
            ctx.drawImage(pieza,piece.xposition*100,piece.yposition*100,100,100);


            })

            window.addEventListener("mousedown",oMousePos);

    }



    const oMousePos = (evt) => {
        var canvas = document.getElementById("canvas");

        var ClientRect = canvas.getBoundingClientRect();

        var x = Math.floor(Math.round(evt.clientX - ClientRect.left)/100);
        var y = Math.floor(Math.round(evt.clientY - ClientRect.top)/100);

        if(form.id!=0){
            movimientos.map(movimiento =>{

                if(movimiento[0]== x && movimiento[1]== y){
                    setForm({...form,xposition:movimiento[0], yposition:movimiento[1]})
                    window.removeEventListener("mousedown",oMousePos);

                    
                }
            })

        }

        pieces.map(piece =>{


            if(piece.xposition == x && piece.yposition == y){
                setForm({id:piece.id,xposition:"-1",yposition:"-1"});
                window.removeEventListener("mousedown",oMousePos);
            

            }


        })


        


       }



    

    
    
    function tablero() {
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080" + sampleLocation.pathname;
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
        .then( response =>{
            setPieces(response.data.pieces);
            setDone("true");
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


    
    const handleButton =() => {


        const token = localStorage.getItem("jwtToken");
        
        let url = "http://localhost:8080/games/move";
            
                    
                        
        axios.post(url,form,
                    {
                        headers: {
                            "Authorization": `Bearer  ${token}`
                        }
            
                    })
                    
            setReload("true");
            
            
                
        }

    

       


    
        


    useEffect(() => {
        tablero();

        if(form.id!=0){
            listaMovimientos();

            if(form.xposition != "-1"){
                handleButton();
            }
        }


        

        return () => {
            setReload("false");
            setForm({id:"0"});
        }

        
    },[form.id,form.xposition,reload])






    

 
    

    
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

            {done === "true" &&
            <div>
                <DrawBoard /> 

            </div>
                
                
            }

            </div>
            



        
        </React.Fragment>
        
        


        
        )

    
}

export default Game;