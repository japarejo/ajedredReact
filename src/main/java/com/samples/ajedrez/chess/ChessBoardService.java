package com.samples.ajedrez.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    public void comprobarCasilla(int x, int y, int chessBoardId){

        Optional<Piece> piezaComer = pieceRepository.existePiezaPosicion(x, y, chessBoardId);

        if(piezaComer.isPresent()){
            pieceRepository.remove(piezaComer.get().getId());

        }


    }



    /* public Boolean esJaque(String color,Piece pieza){



        List<Piece>piezasJugador = this.pieceRepository.piezasJugador(color, pieza.getBoard().getId());

        Piece reyContrario = this.pieceRepository.piezaReyContrario(color, pieza.getBoard().getId());

        List<Integer> posicionReyContrario = new ArrayList<>();

        posicionReyContrario.add(reyContrario.getXPosition());
        posicionReyContrario.add(reyContrario.getYPosition());

        Boolean res = piezasJugador.stream().anyMatch(x-> listaMovimientos(x).contains(posicionReyContrario));



        return res;
    } */


    
    /* public Boolean esJaqueMate(String color, Piece pieza){

        List<Piece>piezasJugador = this.pieceRepository.piezasJugador(color, pieza.getBoard().getId());

        List<List<List<Integer>>> movimientosJugador = piezasJugador.stream().collect(Collectors.mapping(x->listaMovimientos(x),Collectors.toList()));

        Piece reyContrario = this.pieceRepository.piezaReyContrario(color, pieza.getBoard().getId());

        List<List<Integer>> movimientosReyContrario = listaMovimientos(reyContrario);


        Boolean res = movimientosReyContrario.stream().allMatch(movimiento -> movimientosJugador.stream().anyMatch(x-> x.contains(movimiento)));

        //si el rey esta inmovilizado, tenemos que ver si alguna pieza puede cortarlo
        if(res){
            List<Integer> posicionReyContrario = new ArrayList<>();

            posicionReyContrario.add(reyContrario.getXPosition());
            posicionReyContrario.add(reyContrario.getYPosition());

            List<Piece> piezasAtacante = piezasJugador.stream().filter(x->listaMovimientos(x).contains(posicionReyContrario)).collect(Collectors.toList());

            // Si hay mas de una pieza amenazando al rey, y el rey no puede moverse, es jaque mate.
            if(piezasAtacante.size() == 1){
                Piece piezaAtacante = piezasAtacante.get(0);

                if(piezaAtacante.getType().equals("TOWER") || piezaAtacante.getType().equals("BISHOP") || piezaAtacante.getType().equals("QUEEN")){
                    res = anularJaque(piezaAtacante,reyContrario);
                
                } else if(piezaAtacante.getType().equals("HORSE") || piezaAtacante.getType().equals("PAWN")){
                    List<Integer> posicionPiezaAtacante = new ArrayList<>();

                    posicionPiezaAtacante.add(piezaAtacante.getXPosition());
                    posicionPiezaAtacante.add(piezaAtacante.getYPosition());

                    List<Piece> piezasJugadorDefensor = this.pieceRepository.piezasJugador(reyContrario.getColor(), reyContrario.getBoard().getId());

                    // Si devuelve true es que alguna pieza que no sea el rey se puede comer a la pieza atacante. por tanto devolvemos false para indicar que no es jaque mate

                    res = !piezasJugadorDefensor.stream().filter(piezaDefensor -> !piezaDefensor.getType().equals("KING")).anyMatch(piezaDefensor-> listaMovimientos(piezaDefensor).contains(posicionPiezaAtacante));
                    

            }

        }

    }
                                                                        
        return res;

    } */


    //Analizamos si alguna de las piezas puede interponerse o comerse a la pieza atacante
   /*  public Boolean anularJaque(Piece piezaAtacante, Piece reyContrario){

        List<List<Integer>> ls = new ArrayList<>();
        List<Integer> posicion = new ArrayList<>();

        posicion.add(piezaAtacante.getXPosition());
        posicion.add(piezaAtacante.getYPosition());
        ls.add(new ArrayList<>(posicion));
        posicion.clear();


        //Vemos si el jaque se produce por la fila
        if (piezaAtacante.getYPosition() == reyContrario.getYPosition()){
            int inicioIntermedio = Math.min(piezaAtacante.getXPosition(), reyContrario.getXPosition()) + 1;
            int finIntermedio = Math.max(piezaAtacante.getXPosition(), reyContrario.getXPosition()) - 1;

            for(int x = inicioIntermedio; x<= finIntermedio; x++){

                posicion.add(x);
                posicion.add(piezaAtacante.getYPosition());
                ls.add(new ArrayList<>(posicion));

                posicion.clear();
            }

        }


        //Vemos si se produce por la columna
        else if (piezaAtacante.getXPosition() == reyContrario.getXPosition()){
            int inicioIntermedio = Math.min(piezaAtacante.getYPosition(), reyContrario.getYPosition()) + 1;
            int finIntermedio = Math.max(piezaAtacante.getYPosition(), reyContrario.getYPosition()) - 1;

            for(int y = inicioIntermedio; y<= finIntermedio; y++){

                posicion.add(piezaAtacante.getXPosition());
                posicion.add(y);
                ls.add(new ArrayList<>(posicion));

                posicion.clear();
            }

        }


        //Vemos si se produce por la diagonal
        else if (Math.abs(piezaAtacante.getXPosition() - reyContrario.getXPosition()) == Math.abs(piezaAtacante.getYPosition() - reyContrario.getYPosition())){

            int inicioEjeX = Math.min(piezaAtacante.getXPosition(), reyContrario.getXPosition()) + 1 ;
            int finEjeX = Math.max(piezaAtacante.getXPosition(), reyContrario.getXPosition()) - 1 ;

            int inicioEjeY = Math.min(piezaAtacante.getYPosition(), reyContrario.getYPosition()) + 1 ;
            int finEjeY = Math.max(piezaAtacante.getYPosition(), reyContrario.getYPosition()) - 1 ;

            for(int x = inicioEjeX, y = inicioEjeY; x<= finEjeX && y<=finEjeY; x++,y++){
                
                posicion.add(x);
                posicion.add(y);
                ls.add(new ArrayList<>(posicion));

                posicion.clear();
            }


        }


        List<Piece> piezasJugadorDefensor = this.pieceRepository.piezasJugador(reyContrario.getColor(), reyContrario.getBoard().getId());

        Boolean res = !ls.stream().anyMatch(movimiento -> piezasJugadorDefensor.stream().filter(piezaDefensor -> !piezaDefensor.getType().equals("KING")).anyMatch(pieza-> listaMovimientos(pieza).contains(movimiento)));

        return res;




    } */
    




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

        int color = piece.getColor().equals("WHITE")? 10: 11;


        for(int x=0;x<8;x++){
            for(int y = 0; y<8;y++){

                if(Math.abs(posX - x) < 2 && Math.abs(posY - y) < 2){

                    if(tablero[x][y] == color){
                            continue;
                    }
                    
                        
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));

                    posicion.clear();



                }
            }

        }


        return ls;
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
                    if(!(piezaRival.getXPosition() == mov.get(0) && piezaRival.getYPosition() == mov.get(1))){
                        
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
                    if(!(piezaRival.getXPosition() == mov.get(0) && piezaRival.getYPosition() == mov.get(1))){
                        
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
                    if(!(piezaRival.getXPosition() == mov.get(0) && piezaRival.getYPosition() == mov.get(1))){
                        
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

        String color = piece.getColor();

        int x = piece.getXPosition();
        int y = piece.getYPosition();

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
            }


            if(x+1<=7 && y-1>=0 && tablero[x+1][y-1] == 11){
                posicion.clear();
                posicion.add(x+1);
                posicion.add(y-1);
                ls.add(new ArrayList<>(posicion));
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
            }


           if(x-1 >=0 && y+1<=7 && tablero[x-1][y+1] == 10){
                posicion.clear();
                posicion.add(x-1);
                posicion.add(y+1);
                ls.add(new ArrayList<>(posicion));
            }
        }


        return ls;

        }

        



    



    
}
