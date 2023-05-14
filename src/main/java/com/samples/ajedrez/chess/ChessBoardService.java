package com.samples.ajedrez.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessBoardService {

    @Autowired
    ChessBoardRepository chessRepository;


    @Autowired
    PieceRepository pieceRepository;

    public Optional<ChessBoard> findById(Integer id){
        return chessRepository.findById(id);
    }


    public Piece findPieceById(int id){
        return pieceRepository.findById(id);
    }

    public void saveChessBoard(ChessBoard chessBoard){
        this.chessRepository.save(chessBoard);
    }


    public void savePiece(Piece piece){
        this.pieceRepository.save(piece);
    }


    public void comprobarCasilla(int x, int y, int chessBoardId,String color){

        Optional<Piece> piezaComer = pieceRepository.comprobarCasilla(x, y, chessBoardId,color);

        if(piezaComer.isPresent()){
            pieceRepository.remove(piezaComer.get().getId());

        }


    }


    public Optional<Piece> piezaPosicion(int x, int y, int chessBoardId){
        return pieceRepository.existePiezaPosicion(x, y, chessBoardId);
    }

    public Optional<Piece> peonPaso(int chessBoardId){
        return pieceRepository.peonPaso(chessBoardId);
    }





    public Boolean esJaque(String color,Piece pieza, int[][] tablero){



        List<Piece>piezasJugador = this.pieceRepository.piezasJugador(color, pieza.getBoard().getId());

        System.out.println(piezasJugador);

        Piece rey = this.pieceRepository.piezaReyContrario(color, pieza.getBoard().getId()); //Miro si  despues de haber hecho el movimiento hay opcion de llegar al rey con alguna de mis piezas

        List<Integer> posRey = new ArrayList<>();

        posRey.add(rey.getXPosition());
        posRey.add(rey.getYPosition());

        Boolean res = false;

        for(Piece p: piezasJugador){
            if(listaMovimientos(p, tablero).contains(posRey)){
                res = true;
                break;
            }
        }



        return res;
    }


    
    public Boolean esJaqueMate(String color, Piece pieza, int[][] tablero){

        List<Piece> piezasRival = this.pieceRepository.piezasRival(color, pieza.getBoard().getId()); // Saco las piezas del oponente, que es al que le toca moverse

        Boolean res = true;

        for(Piece p: piezasRival){ //Miro si alguna de las piezas del oponente tiene algún movimiento válido, si es así, no es jaque mate.
            
            if(!listaMovimientos(p, tablero).isEmpty()){
                res = false;
                break;
            }
        }
                                                                        
        return res;

    }




    public List<List<Integer>> listaMovimientos(Piece piece, int[][] tablero){


        List<List<Integer>> ls = new ArrayList<>();

        if(piece.getType().equals("PAWN")){

            ls =  calculaMovimientoPeon(piece,tablero);
            
        } else if(piece.getType().equals("HORSE")){

            ls = calculaMovimientoCaballo(piece,tablero);
        
        } else if(piece.getType().equals("TOWER")){
            ls = calculaMovimientoTorre(piece,tablero);
        
        } else if(piece.getType().equals("BISHOP")){
            ls = calculaMovimientoAlfil(piece,tablero);
        
        } else if(piece.getType().equals("QUEEN")){
            ls = calculaMovimientoAlfil(piece,tablero);

            ls.addAll(calculaMovimientoTorre(piece,tablero));
        
        } else if(piece.getType().equals("KING")){
            ls = calculaMovimientoRey(piece,tablero);
        }
        
        return ls;
    }



    private List<List<Integer>> calculaMovimientoRey(Piece piece, int[][] tablero) {

        List<List<Integer>> ls = new ArrayList<>();
        List<Integer> posicion = new ArrayList<>();

        int posX = piece.getXPosition();
        int posY = piece.getYPosition();

        int indiceColor = piece.getColor().equals("WHITE")? 10: 11;

        String color = piece.getColor();

        Boolean enroqueCorto = false;
        Boolean enroqueLargo = false;

        if(!piece.getBoard().getJaque() && piece.getColor().equals(piece.getBoard().getTurn())){
            if(!piece.getPiezaMovida() && tablero[posX-1][posY] == 0 && tablero[posX-2][posY] == 0 && tablero[posX-3][posY] == 0 && tablero[posX-4][posY] == indiceColor){ //Analizo el enroque largo. Si el rey esta en jaque 
            
                Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(posX-4, posY,piece.getBoard().getId());
    
                if(piezaPosicion.isPresent() && piezaPosicion.get().getType().equals("TOWER") && !piezaPosicion.get().getPiezaMovida()){
                    posicion.add(posX-2);
                    posicion.add(posY);
                    ls.add(new ArrayList<>(posicion));
    
                    posicion.clear();
    
                    enroqueLargo = true;
                }
    
    
    
            }if(!piece.getPiezaMovida() && tablero[posX+1][posY] == 0 && tablero[posX+2][posY] == 0 && tablero[posX+3][posY] == indiceColor){ //Analizo el enroque corto
                
                Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(posX+3, posY, piece.getBoard().getId());
    
                if(piezaPosicion.isPresent() && piezaPosicion.get().getType().equals("TOWER") && !piezaPosicion.get().getPiezaMovida()){
                    posicion.add(posX+2);
                    posicion.add(posY);
                    ls.add(new ArrayList<>(posicion));
    
                    posicion.clear();
    
                    enroqueCorto = true;
                }
            }
        }
        
        
        


        for(int x=0;x<8;x++){
            for(int y = 0; y<8;y++){

                if(Math.abs(posX - x) < 2 && Math.abs(posY - y) < 2){

                    if(tablero[x][y] == indiceColor){
                            continue;
                    }
                    
                        
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));

                    posicion.clear();



                }
            }

        }
        
        List<List<Integer>> movimientosValidos =  new ArrayList<>(ls);

        if(piece.getColor().equals(piece.getBoard().getTurn())){
            
            List<Piece> piezasRival = this.pieceRepository.piezasRival(color,piece.getBoard().getId());

            for(List<Integer>mov: ls){

                if(Math.abs(posX - mov.get(0)) == 2){ // Si el valor del eje x cambia en 2 unidades es que estamos analizando el enroque
                    if(enroqueLargo){
                        tablero[3][posY] = indiceColor; // Rellenamos la posicion donde se iría la torre
                        tablero[0][posY] = 0; // La posicion donde estaba la torre la ponemos a 0
                    
                    }if(enroqueCorto){
                        tablero[5][posY] = indiceColor; // Rellenamos la posicion donde se iría la torre
                        tablero[7][posY] = 0;
                    }
                }
                
                
                int valorMovPieza = tablero[mov.get(0)][mov.get(1)];

                tablero[mov.get(0)][mov.get(1)] = indiceColor;
                tablero[posX][posY] = 0;

                
                for(Piece piezaRival: piezasRival){
                    if(!(piezaRival.getXPosition() == mov.get(0) && piezaRival.getYPosition() == mov.get(1)) && ( piezaRival.getXPosition() == mov.get(0)
                    || piezaRival.getYPosition() == mov.get(1) || (Math.abs(piezaRival.getXPosition() - mov.get(0)) == Math.abs(piezaRival.getYPosition() - mov.get(1)))
                    || piezaRival.getType().equals("HORSE"))){
                        
                        if(listaMovimientos(piezaRival,tablero).contains(mov)){
                            
                            movimientosValidos.remove(mov);
                        }
                    }
                    
                    
                }

                if(Math.abs(posX - mov.get(0)) == 2){
                    if(enroqueLargo){
                        tablero[3][posY] = 0; // Volvemos a poner a 0 la posible casilla hacia la que haria el enroque la torre
                        tablero[0][posY] = indiceColor; // La posicion donde estaba la torre le volvemos a poner su valor para la siguiente iteracion
                
                    }if(enroqueCorto){
                        tablero[5][posY] = 0; 
                        tablero[7][posY] = indiceColor;
                    }
                }

                tablero[mov.get(0)][mov.get(1)] = valorMovPieza;
                tablero[posX][posY] = indiceColor;




            }
            //Tengo que ver si es enroque, si la posicion por la que pasa el rey esta amenazada, ya que solo puede enrocarse si el rey no esta amenazado ni ninguna de las posiciones por las que pasa.

            if (enroqueCorto){
                List<Integer> pos1 = new ArrayList<>();

                pos1.add(posX+1);
                pos1.add(posY);

                List<Integer> pos2 = new ArrayList<>();

                pos2.add(posX+2);
                pos2.add(posY);


                if(movimientosValidos.contains(pos2) && !movimientosValidos.contains(pos1)){
                    movimientosValidos.remove(pos2);
                }

            } else if (enroqueLargo){
                List<Integer> pos1 = new ArrayList<>();

                pos1.add(posX-1);
                pos1.add(posY);

                List<Integer> pos2 = new ArrayList<>();

                pos2.add(posX-2);
                pos2.add(posY);


                if(movimientosValidos.contains(pos2) && !movimientosValidos.contains(pos1)){
                    movimientosValidos.remove(pos2);
                }
            }
        }

        

        return movimientosValidos;

    }


    private List<List<Integer>> calculaMovimientoAlfil(Piece piece, int[][]tablero){
        
        List<List<Integer>> ls = new ArrayList<>();
        List<Integer> posicion = new ArrayList<>();

        int posXActual = piece.getXPosition();
        int posYActual = piece.getYPosition();

        int indiceColor = piece.getColor().equals("WHITE")? 10: 11;

        String color = piece.getColor();

        int indiceColorOpuesto = indiceColor == 10? 11: 10;

        //Vemos la diagonal superior derecha
        for(int x = posXActual +1, y = posYActual -1; x<=7 && y>=0; x++, y--){

            if(tablero[x][y] == indiceColor){
                break;
            
            }
            posicion.add(x);
            posicion.add(y);
            ls.add(new ArrayList<>(posicion));
            posicion.clear();

            if(tablero[x][y] == indiceColorOpuesto){
                break;
            }
        
        }



        //Vemos la diagonal inferior izquierda
        for(int x = posXActual -1, y = posYActual +1; x>=0 && y<=7; x--, y++){

            if(tablero[x][y] == indiceColor){
                break;
            
            }
            posicion.add(x);
            posicion.add(y);
            ls.add(new ArrayList<>(posicion));
            posicion.clear();

            if(tablero[x][y] == indiceColorOpuesto){
                break;
            }
        
        }



        //Vemos la diagonal superior izquierda
        for(int x = posXActual -1, y = posYActual -1; x>=0 && y>=0; x--, y--){

            if(tablero[x][y] == indiceColor){
                break;
            
            }
            posicion.add(x);
            posicion.add(y);
            ls.add(new ArrayList<>(posicion));
            posicion.clear();

            if(tablero[x][y] == indiceColorOpuesto){
                break;
            }
        
        }




        //Vemos la diagonal inferior derecha
        for(int x = posXActual +1, y = posYActual +1; x<=7 && y<=7; x++, y++){

            if(tablero[x][y] == indiceColor){
                break;
            
            }
            posicion.add(x);
            posicion.add(y);
            ls.add(new ArrayList<>(posicion));
            posicion.clear();

            if(tablero[x][y] == indiceColorOpuesto){
                break;
            }
        
        }

        List<List<Integer>> movimientosValidos =  new ArrayList<>(ls);

        if(piece.getColor().equals(piece.getBoard().getTurn())){
            
            List<Piece> piezasRival = this.pieceRepository.piezasRival(color,piece.getBoard().getId());

            Piece posicionRey = this.pieceRepository.piezaRey(color, piece.getBoard().getId());

            List<Integer> posRey = new ArrayList<>();

            posRey.add(posicionRey.getXPosition());
            posRey.add(posicionRey.getYPosition());

            for(List<Integer>mov: ls){
                
                int valorMovPieza = tablero[mov.get(0)][mov.get(1)];

                tablero[mov.get(0)][mov.get(1)] = indiceColor;
                tablero[posXActual][posYActual] = 0;

                
                for(Piece piezaRival: piezasRival){
                    if(!(piezaRival.getXPosition() == mov.get(0) && piezaRival.getYPosition() == mov.get(1)) && ( piezaRival.getXPosition() == posRey.get(0)
                        || piezaRival.getYPosition() == posRey.get(1) || (Math.abs(piezaRival.getXPosition() - posRey.get(0)) == Math.abs(piezaRival.getYPosition() - posRey.get(1)))
                        || piezaRival.getType().equals("HORSE"))){
                        
                        if(listaMovimientos(piezaRival,tablero).contains(posRey)){
                            
                            movimientosValidos.remove(mov);
                        }
                    }
                    
                    
                }

                tablero[mov.get(0)][mov.get(1)] = valorMovPieza;
                tablero[posXActual][posYActual] = indiceColor;




            }

        }

        return movimientosValidos;

    }





    private List<List<Integer>> calculaMovimientoTorre(Piece piece,int[][]tablero) {
        List<List<Integer>> ls = new ArrayList<>();

        List<Integer> posicion = new ArrayList<>();

        int posXActual = piece.getXPosition();
        int posYActual = piece.getYPosition();


        int indiceColor = piece.getColor().equals("WHITE")? 10: 11;

        String color = piece.getColor();

        int indiceColorOpuesto = indiceColor == 10? 11: 10;

        //Vemos la vertical hacia arriba

        for(int posYNueva=posYActual-1;posYNueva>=0;posYNueva--){

            if(tablero[posXActual][posYNueva] == indiceColor){
                break;
            
            }
            posicion.add(posXActual);
            posicion.add(posYNueva);
            ls.add(new ArrayList<>(posicion));
            posicion.clear();

            if(tablero[posXActual][posYNueva] == indiceColorOpuesto){
                break;
            }
        }
        


        //Vemos la vertical hacia abajo

        for(int posYNueva=posYActual+1;posYNueva<=7;posYNueva++){

            if(tablero[posXActual][posYNueva] == indiceColor){
                break;
            
            }
            posicion.add(posXActual);
            posicion.add(posYNueva);
            ls.add(new ArrayList<>(posicion));
            posicion.clear();

            if(tablero[posXActual][posYNueva] == indiceColorOpuesto){
                break;
            }
        }



        
        //Vemos la horizontal hacia la izquierda

        for(int posXNueva=posXActual-1;posXNueva>=0;posXNueva--){

            if(tablero[posXNueva][posYActual] == indiceColor){
                break;
            
            }
            posicion.add(posXNueva);
            posicion.add(posYActual);
            ls.add(new ArrayList<>(posicion));
            posicion.clear();

            if(tablero[posXNueva][posYActual] == indiceColorOpuesto){
                break;
            }
        }


        //Vemos la horizontal hacia la derecha

        for(int posXNueva=posXActual+1;posXNueva<=7;posXNueva++){

            if(tablero[posXNueva][posYActual] == indiceColor){
                break;
            
            }
            posicion.add(posXNueva);
            posicion.add(posYActual);
            ls.add(new ArrayList<>(posicion));
            posicion.clear();

            if(tablero[posXNueva][posYActual] == indiceColorOpuesto){
                break;
            }
        }

        List<List<Integer>> movimientosValidos =  new ArrayList<>(ls);

        if(piece.getColor().equals(piece.getBoard().getTurn())){
            
            List<Piece> piezasRival = this.pieceRepository.piezasRival(color,piece.getBoard().getId());

            Piece posicionRey = this.pieceRepository.piezaRey(color, piece.getBoard().getId());

            List<Integer> posRey = new ArrayList<>();

            posRey.add(posicionRey.getXPosition());
            posRey.add(posicionRey.getYPosition());

            for(List<Integer>mov: ls){
                
                int valorMovPieza = tablero[mov.get(0)][mov.get(1)];

                tablero[mov.get(0)][mov.get(1)] = indiceColor;
                tablero[posXActual][posYActual] = 0;

                
                for(Piece piezaRival: piezasRival){
                    if(!(piezaRival.getXPosition() == mov.get(0) && piezaRival.getYPosition() == mov.get(1)) && ( piezaRival.getXPosition() == posRey.get(0)
                    || piezaRival.getYPosition() == posRey.get(1) || (Math.abs(piezaRival.getXPosition() - posRey.get(0)) == Math.abs(piezaRival.getYPosition() - posRey.get(1)))
                    || piezaRival.getType().equals("HORSE"))){
                        
                        if(listaMovimientos(piezaRival,tablero).contains(posRey)){
                            
                            movimientosValidos.remove(mov);
                        }
                    }
                    
                    
                }

                tablero[mov.get(0)][mov.get(1)] = valorMovPieza;
                tablero[posXActual][posYActual] = indiceColor;




            }

        }

        return movimientosValidos;


    }


    private List<List<Integer>> calculaMovimientoCaballo(Piece piece, int[][]tablero) {
        List<List<Integer>> ls = new ArrayList<>();

        List<Integer> posicion = new ArrayList<>();

        int posX = piece.getXPosition();
        int posY = piece.getYPosition();

        int indiceColor = piece.getColor().equals("WHITE")? 10: 11;

        String color = piece.getColor();

        for(int x=0;x<8;x++){
            for(int y = 0; y<8;y++){

                if(Math.abs(posX - x) == 2 && Math.abs(posY - y) == 1 || 
                Math.abs(posX - x) == 1 && Math.abs(posY - y) == 2){

                    if(tablero[x][y] == indiceColor){
                        continue;
                    }
            
                        
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));

                    posicion.clear();
                    
                }
            }

        }


        List<List<Integer>> movimientosValidos =  new ArrayList<>(ls);

        if(piece.getColor().equals(piece.getBoard().getTurn())){
            
            List<Piece> piezasRival = this.pieceRepository.piezasRival(color,piece.getBoard().getId());

            Piece posicionRey = this.pieceRepository.piezaRey(color, piece.getBoard().getId());

            List<Integer> posRey = new ArrayList<>();

            posRey.add(posicionRey.getXPosition());
            posRey.add(posicionRey.getYPosition());

            for(List<Integer>mov: ls){
                
                int valorMovPieza = tablero[mov.get(0)][mov.get(1)];

                tablero[mov.get(0)][mov.get(1)] = indiceColor;
                tablero[posX][posY] = 0;

                
                for(Piece piezaRival: piezasRival){
                    if(!(piezaRival.getXPosition() == mov.get(0) && piezaRival.getYPosition() == mov.get(1)) && ( piezaRival.getXPosition() == posRey.get(0)
                        || piezaRival.getYPosition() == posRey.get(1) || (Math.abs(piezaRival.getXPosition() - posRey.get(0)) == Math.abs(piezaRival.getYPosition() - posRey.get(1)))
                        || piezaRival.getType().equals("HORSE"))){
                        
                        if(listaMovimientos(piezaRival,tablero).contains(posRey)){
                            
                            movimientosValidos.remove(mov);
                        }
                    }
                    
                    
                }

                tablero[mov.get(0)][mov.get(1)] = valorMovPieza;
                tablero[posX][posY] = indiceColor;




            }

        }

        return movimientosValidos;
    }


    private List<List<Integer>> calculaMovimientoPeon(Piece piece,int[][]tablero){

        List<List<Integer>> ls = new ArrayList<>();

        List<Integer> posicion = new ArrayList<>();

        int indiceColor = piece.getColor().equals("WHITE")? 10: 11;

        String color = piece.getColor();

        int x = piece.getXPosition();
        int y = piece.getYPosition();

        Boolean peonPasoDiagonal1 = false;
        Boolean peonPasoDiagonal2 = false;

        Optional<Piece> peonPaso = this.peonPaso(piece.getBoard().getId());

        if(color.equals("WHITE")){


            if(y-1 >= 0 && tablero[x][y-1] == 0) {
                posicion.add(x);
                posicion.add(y-1);
                ls.add(new ArrayList<>(posicion));

                if(y==6){
                    posicion.clear();
                
                    if(tablero[x][y-2] == 0){
                        posicion.add(x);
                        posicion.add(y-2);
                        ls.add(new ArrayList<>(posicion));
                    }
                }


            }


            if(x-1>=0 && y-1>=0 && tablero[x-1][y-1] == 11){
                posicion.clear();
                posicion.add(x-1);
                posicion.add(y-1);
                ls.add(new ArrayList<>(posicion));
            
            }else if(x-1>=0 && y-1>=0 && tablero[x-1][y-1] == 0){


                if(peonPaso.isPresent() && peonPaso.get().getXPosition() == x-1 && peonPaso.get().getYPosition() == y){
                    
                    peonPasoDiagonal1 = true;
                    posicion.clear();
                    posicion.add(x-1);
                    posicion.add(y-1);
                    ls.add(new ArrayList<>(posicion));
                }
                
                
            }


            




            if(x+1<=7 && y-1>=0 && tablero[x+1][y-1] == 11){
                posicion.clear();
                posicion.add(x+1);
                posicion.add(y-1);
                ls.add(new ArrayList<>(posicion));
            }else if(x+1<=7 && y-1>=0 && tablero[x+1][y-1] == 0){

                if(peonPaso.isPresent() && peonPaso.get().getXPosition() == x+1 && peonPaso.get().getYPosition() == y){
               
                    peonPasoDiagonal2 = true;
                    posicion.clear();
                    posicion.add(x+1);
                    posicion.add(y-1);
                    ls.add(new ArrayList<>(posicion));
                }
            }


            




            
        }else{


            if(y+1 <= 7 && tablero[x][y+1] == 0){
                posicion.add(x);
                posicion.add(y+1);
                ls.add(new ArrayList<>(posicion));

               
            
                if(y==1){
                    posicion.clear();

                    if(tablero[x][y+2] == 0){

                        posicion.add(x);
                        posicion.add(y+2);
                        ls.add(new ArrayList<>(posicion));
                    }
                }
            }



            if(x+1 <=7 && y+1<=7 && tablero[x+1][y+1] == 10){
                posicion.clear();
                posicion.add(x+1);
                posicion.add(y+1);
                ls.add(new ArrayList<>(posicion));
            
            } else if(x+1 <=7 && y+1<=7 && tablero[x+1][y+1] == 0){

                if(peonPaso.isPresent() && peonPaso.get().getXPosition() == x+1 && peonPaso.get().getYPosition() == y){
                    peonPasoDiagonal2 = true;
                    posicion.clear();
                    posicion.add(x+1);
                    posicion.add(y+1);
                    ls.add(new ArrayList<>(posicion));
                }
            }


           if(x-1 >=0 && y+1<=7 && tablero[x-1][y+1] == 10){
                posicion.clear();
                posicion.add(x-1);
                posicion.add(y+1);
                ls.add(new ArrayList<>(posicion));
            
            }else if(x-1 >=0 && y+1<=7 && tablero[x-1][y+1] == 0){

                if(peonPaso.isPresent() && peonPaso.get().getXPosition() == x-1 && peonPaso.get().getYPosition() == y){
                    peonPasoDiagonal1 = true;
                    posicion.clear();
                    posicion.add(x-1);
                    posicion.add(y+1);
                    ls.add(new ArrayList<>(posicion));
                }
            }
        }
        
        List<List<Integer>> movimientosValidos =  new ArrayList<>(ls);

        if(piece.getColor().equals(piece.getBoard().getTurn())){
            
            List<Piece> piezasRival = this.pieceRepository.piezasRival(color,piece.getBoard().getId());

            Piece posicionRey = this.pieceRepository.piezaRey(color, piece.getBoard().getId());

            List<Integer> posRey = new ArrayList<>();

            posRey.add(posicionRey.getXPosition());
            posRey.add(posicionRey.getYPosition());

            for(List<Integer>mov: ls){
                
                int valorMovPieza = tablero[mov.get(0)][mov.get(1)];

                int indiceY = piece.getColor().equals("WHITE")? y+1:y-1; // Si es blanco,miramos la casilla que esta detras y por tanto incrementa el eje y. Si es negro al reves. Se utiliza para la captura del peon al paso

                int indiceColorOpuesto = piece.getColor().equals("WHITE")? 11:10; // Si es blanco, nos quedamos con el 11, si es blanco con el 10. Se utiliza para la captura del peon al paso

                if(mov.get(0) != x){ // Si el valor del eje x cambia quiere decir que el peon se mueve en diagonal
                    if((peonPasoDiagonal1 || peonPasoDiagonal2) && tablero[mov.get(0)][mov.get(1)] == 0){
                        tablero[mov.get(0)][indiceY] = 0;
                    } 
                }

                tablero[mov.get(0)][mov.get(1)] = indiceColor;
                tablero[x][y] = 0;

                

                
                for(Piece piezaRival: piezasRival){
                    if(!(piezaRival.getXPosition() == mov.get(0) && piezaRival.getYPosition() == mov.get(1)) && ( piezaRival.getXPosition() == posRey.get(0)
                        || piezaRival.getYPosition() == posRey.get(1) || (Math.abs(piezaRival.getXPosition() - posRey.get(0)) == Math.abs(piezaRival.getYPosition() - posRey.get(1)))
                        || piezaRival.getType().equals("HORSE"))){
                        
                        if(listaMovimientos(piezaRival,tablero).contains(posRey)){
                            
                            movimientosValidos.remove(mov);
                        }
                    }
                    
                    
                }

                tablero[mov.get(0)][mov.get(1)] = valorMovPieza;
                tablero[x][y] = indiceColor;

                if(mov.get(0) != x){ // Si el valor del eje x cambia quiere decir que el peon se mueve en diagonal
                    if((peonPasoDiagonal1 || peonPasoDiagonal2) && tablero[mov.get(0)][mov.get(1)] == 0){
                        tablero[mov.get(0)][indiceY] = indiceColorOpuesto;
                    } 
                }




            }

        }

        return movimientosValidos;

        }

        public void inicializacionTablero(ChessBoard board){

            this.saveChessBoard(board);
    
    
            for(int i=0; i<8;i++){
                Piece piece = new Piece();
    
                piece.setColor("WHITE");
                piece.setType("PAWN");
                piece.setXPosition(i);
                piece.setYPosition(6);
                piece.setBoard(board);
                piece.setPiezaMovida(false);
                piece.setPeonPaso(false);
                this.pieceRepository.save(piece);
            }
    
    
            for(int i=0; i<8;i++){
                Piece piece = new Piece();
    
                piece.setColor("BLACK");
                piece.setType("PAWN");
                piece.setXPosition(i);
                piece.setYPosition(1);
                piece.setBoard(board);
                piece.setPiezaMovida(false);
                piece.setPeonPaso(false);
                this.pieceRepository.save(piece);
            }
    
    
            Piece piece1 = new Piece();
            piece1.setColor("WHITE");
            piece1.setType("KING");
            piece1.setXPosition(4);
            piece1.setYPosition(7);
            piece1.setBoard(board);
            piece1.setPiezaMovida(false);
            piece1.setPeonPaso(false);
    
    
            Piece piece2 = new Piece();
            piece2.setColor("BLACK");
            piece2.setType("KING");
            piece2.setXPosition(4);
            piece2.setYPosition(0);
            piece2.setBoard(board);
            piece2.setPiezaMovida(false);
            piece2.setPeonPaso(false);
    
            Piece piece3 = new Piece();
            piece3.setColor("WHITE");
            piece3.setType("QUEEN");
            piece3.setXPosition(3);
            piece3.setYPosition(7);
            piece3.setBoard(board);
            piece3.setPiezaMovida(false);
            piece3.setPeonPaso(false);
    
    
            Piece piece4 = new Piece();
            piece4.setColor("BLACK");
            piece4.setType("QUEEN");
            piece4.setXPosition(3);
            piece4.setYPosition(0);
            piece4.setBoard(board);
            piece4.setPiezaMovida(false);
            piece4.setPeonPaso(false);
    
    
            for(int i=2;i<6;i=i+3){
                Piece piece = new Piece();
                piece.setColor("BLACK");
                piece.setType("BISHOP");
                piece.setXPosition(i);
                piece.setYPosition(0);
                piece.setBoard(board);
                piece.setPiezaMovida(false);
                piece.setPeonPaso(false);
                this.pieceRepository.save(piece);
            }
    
            for(int i=2;i<6;i=i+3){
                Piece piece = new Piece();
                piece.setColor("WHITE");
                piece.setType("BISHOP");
                piece.setXPosition(i);
                piece.setYPosition(7);
                piece.setBoard(board);
                piece.setPiezaMovida(false);
                piece.setPeonPaso(false);
                this.pieceRepository.save(piece);
            }
    
            for(int i=1;i<7;i=i+5){
                Piece piece = new Piece();
    
                piece.setColor("BLACK");
                piece.setType("HORSE");
                piece.setXPosition(i);
                piece.setYPosition(0);
                piece.setBoard(board);
                piece.setPiezaMovida(false);
                piece.setPeonPaso(false);
                this.pieceRepository.save(piece);
            }
    
            for(int i=1;i<7;i=i+5){
                Piece piece = new Piece();
    
                piece.setColor("WHITE");
                piece.setType("HORSE");
                piece.setXPosition(i);
                piece.setYPosition(7);
                piece.setBoard(board);
                piece.setPiezaMovida(false);
                piece.setPeonPaso(false);
                this.pieceRepository.save(piece);
            }
            
    
    
            for(int i=0;i<8;i=i+7){
                Piece piece = new Piece();
    
                piece.setColor("WHITE");
                piece.setType("TOWER");
                piece.setXPosition(i);
                piece.setYPosition(7);
                piece.setBoard(board);
                piece.setPiezaMovida(false);
                piece.setPeonPaso(false);
                this.pieceRepository.save(piece);
            }
            
    
            for(int i=0;i<8;i=i+7){
                Piece piece = new Piece();
    
                piece.setColor("BLACK");
                piece.setType("TOWER");
                piece.setXPosition(i);
                piece.setYPosition(0);
                piece.setBoard(board);
                piece.setPiezaMovida(false);
                piece.setPeonPaso(false);
                this.pieceRepository.save(piece);
            }
    
            this.pieceRepository.save(piece1);
            this.pieceRepository.save(piece2);
            this.pieceRepository.save(piece3);
            this.pieceRepository.save(piece4);
        }

        



    



    
}
