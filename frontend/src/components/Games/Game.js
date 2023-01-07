import React, { useEffect, useState } from 'react';

import axios from 'axios';

import { useLocation} from "react-router-dom"

import NavBar from '../../Navbar';





function Game() {

    const [width,setWidth] = useState("");
    const [height,setHeight] = useState("");
    const [pieces,setPieces] = useState([]);


    const[movimientos,setMovimientos] = useState([]);

    const [form,setForm] = useState({
        id: "0",
        xposition:"",
        yposition:""
    })

    const sampleLocation = useLocation();


    


    const drawBoard = () => {
        
        var canvas = document.getElementById("canvas");
        var ctx = canvas.getContext("2d");
        var image = document.getElementById('source');
        ctx.drawImage(image, 0, 0,width, height);






    }


    const drawPiece = () =>{

        var canvas = document.getElementById("canvas");


        movimientos.map(movimiento =>{
            
            var ctx = canvas.getContext("2d");
    
            ctx.fillStyle = '#b0c4de';
            ctx.fillRect(movimiento[0]*100, movimiento[1]*100,100,100);
                
            })

        pieces.map(piece =>{
            var pieza = document.getElementById(piece.type + "-" + piece.color);
            var ctx = canvas.getContext("2d");


            if(piece.id == form.id){
                ctx.fillStyle = '#FF0000';
                ctx.fillRect(piece.xposition*100, piece.yposition*100, 100, 100);
            }



            ctx.drawImage(pieza,piece.xposition*100,piece.yposition*100,100,100);

            })

        
        

        
        
    
    
    
    }


    


    const tablero = () =>{
        const token = localStorage.getItem("jwtToken");

        let url = "http://localhost:8080" + sampleLocation.pathname;
        axios.get(url,{ headers: { "Authorization": `Bearer  ${token}`}})
        .then( response =>{
            setWidth(response.data.width);
            setHeight(response.data.height);
            setPieces(response.data.pieces);
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



    useEffect(() => {
        tablero();
        drawBoard();
        drawPiece();

        if(form.id!=0){
            listaMovimientos();
        }
        
    }, [tablero])


    const handleSubmit = (e) =>{
        e.preventDefault();
      }


    const handleChange = async e =>{

        if(e.target.name==='move'){
            var x = e.target.value.split(",")[0];
            var y = e.target.value.split(",")[1];

            setForm({...form,xposition: x, yposition: y })
        }else{
            setForm({...form,[e.target.name]: e.target.value})
        }
        
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

    }

    

 
    

    
    return(
        
        <React.Fragment>
        
        <NavBar></NavBar>
        <div className="container">
            <br></br>

            <canvas id="canvas" width={width} height={height}> </canvas>
            <img id="source" src={require('../../assets/img/tablero.png')} alt="alt" style={{display:'none'}}></img>

            <img id="HORSE-BLACK" src={require('../../assets/img/HORSE-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="KING-BLACK" src={require('../../assets/img/KING-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="KING-WHITE" src={require('../../assets/img/KING-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="BISHOP-BLACK" src={require('../../assets/img/BISHOP-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="PAWN-BLACK" src={require('../../assets/img/PAWN-BLACK.png')} alt="alt" style={{display:'none'}}/>
            <img id="PAWN-WHITE" src={require('../../assets/img/PAWN-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="TOWER-WHITE" src={require('../../assets/img/TOWER-WHITE.png')} alt="alt" style={{display:'none'}}/>
            <img id="TOWER-BLACK" src={require('../../assets/img/TOWER-BLACK.png')} alt="alt" style={{display:'none'}}/>

            </div>


            <div className="container">

            <form onSubmit={handleSubmit}>
                <label>
                        <h3>Pieza: </h3>     
                        <select name = "id" value={form.id} onChange={handleChange}>{
                        
                            
                            pieces.map(piece =>
                                <option key={piece.id} value={piece.id}>{piece.type}</option>
                            )}
                            <option value="0">Seleccione una</option>
                        </select>
            </label>


            {form.id !== "0" &&
                    
                    
                    <div>
                    <label>
                    <h3>Movimiento: </h3>     
                    <select name = "move"  onChange={handleChange}>{


                        movimientos.map(movimiento =>
                                <option key={movimiento} value={movimiento}>{movimiento[0]},{movimiento[1]}</option>
                            )}
                    
                    <option value="">Seleccione una</option>
                    </select>

                    
                    </label>

                    <input type="submit"  value="Mover" onClick={handleButton}/>

                    </div>
                }

            </form>





            </div>


        
        </React.Fragment>
        
        


        
        )

    
}

export default Game;