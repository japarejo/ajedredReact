package com.samples.ajedrez.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
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



    public Boolean esJaque(String color,Piece pieza){



        List<Piece>piezasJugador = this.pieceRepository.piezasJugador(color, pieza.getBoard().getId());

        Piece reyContrario = this.pieceRepository.piezaReyContrario(color, pieza.getBoard().getId());

        List<Integer> posicionReyContrario = new ArrayList<>();

        posicionReyContrario.add(reyContrario.getXPosition());
        posicionReyContrario.add(reyContrario.getYPosition());

        Boolean res = piezasJugador.stream().anyMatch(x-> listaMovimientos(x).contains(posicionReyContrario));



        return res;
    }


    
    public Boolean esJaqueMate(String color, Piece pieza){

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

    }


    //Analizamos si alguna de las piezas puede interponerse o comerse a la pieza atacante
    public Boolean anularJaque(Piece piezaAtacante, Piece reyContrario){

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
        else if (Math.abs(piezaAtacante.getXPosition() - reyContrario.getYPosition()) == Math.abs(piezaAtacante.getYPosition() - reyContrario.getYPosition())){

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




    }
    




    public List<List<Integer>> listaMovimientos(Piece piece){


        List<List<Integer>> ls = new ArrayList<>();

        if(piece.getType().equals("PAWN")){

            ls =  calculaMovimientoPeon(piece);
            
        } else if(piece.getType().equals("HORSE")){

            ls = calculaMovimientoCaballo(piece);
        
        } else if(piece.getType().equals("TOWER")){
            ls = calculaMovimientoTorre(piece);
        
        } else if(piece.getType().equals("BISHOP")){
            ls = calculaMovimientoAlfil(piece);
        
        } else if(piece.getType().equals("QUEEN")){
            ls = calculaMovimientoAlfil(piece);

            ls.addAll(calculaMovimientoTorre(piece));
        
        } else if(piece.getType().equals("KING")){
            ls = calculaMovimientoRey(piece);
        }
        
        return ls;
    }



    private List<List<Integer>> calculaMovimientoRey(Piece piece) {

        List<List<Integer>> ls = new ArrayList<>();
        List<Integer> posicion = new ArrayList<>();


        for(int x=0;x<8;x++){
            for(int y = 0; y<8;y++){

                if(Math.abs(piece.getXPosition() - x) < 2 && Math.abs(piece.getYPosition() - y) < 2){

                    Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(x, y,piece.getBoard().getId());

                    if(piezaPosicion.isPresent()){

                        if(piezaPosicion.get().color.equals(piece.getColor())){
                            continue;
                        }
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


    private List<List<Integer>> calculaMovimientoAlfil(Piece piece){
        
        List<List<Integer>> ls = new ArrayList<>();
        List<Integer> posicion = new ArrayList<>();

        int posXActual = piece.getXPosition();
        int posYActual = piece.getYPosition();

        //Vemos la diagonal superior derecha
        for(int x = posXActual +1, y = posYActual -1; x<=7 && y>=0; x++, y--){

            Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(x, y,piece.getBoard().getId());

            if(piezaPosicion.isPresent()){
                if(piezaPosicion.get().color.equals(piece.getColor())){
                    break;
                }else if(!piezaPosicion.get().getType().equals("KING")){
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
                }else{
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();
                }
            }else{
                posicion.add(x);
                posicion.add(y);
                ls.add(new ArrayList<>(posicion));
                posicion.clear();
            }
        
        }



        //Vemos la diagonal inferior izquierda
        for(int x = posXActual -1, y = posYActual +1; x>=0 && y<=7; x--, y++){

            Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(x, y,piece.getBoard().getId());

            if(piezaPosicion.isPresent()){
                if(piezaPosicion.get().color.equals(piece.getColor())){
                    break;
                }else if(!piezaPosicion.get().getType().equals("KING")){
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
                }else{
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();
                }
            }else{
                posicion.add(x);
                posicion.add(y);
                ls.add(new ArrayList<>(posicion));
                posicion.clear();
            }
        
        }



        //Vemos la diagonal superior izquierda
        for(int x = posXActual -1, y = posYActual -1; x>=0 && y>=0; x--, y--){

            Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(x, y,piece.getBoard().getId());

            if(piezaPosicion.isPresent()){
                if(piezaPosicion.get().color.equals(piece.getColor())){
                    break;
                }else if(!piezaPosicion.get().getType().equals("KING")){
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
                }else{
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();
                }
            }else{
                posicion.add(x);
                posicion.add(y);
                ls.add(new ArrayList<>(posicion));
                posicion.clear();
            }
        
        }




        //Vemos la diagonal inferior derecha
        for(int x = posXActual +1, y = posYActual +1; x<=7 && y<=7; x++, y++){

            Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(x, y,piece.getBoard().getId());

            if(piezaPosicion.isPresent()){
                if(piezaPosicion.get().color.equals(piece.getColor())){
                    break;
                }else if(!piezaPosicion.get().getType().equals("KING")){
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
                }else{
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();
                }
            }else{
                posicion.add(x);
                posicion.add(y);
                ls.add(new ArrayList<>(posicion));
                posicion.clear();
            }
        
        }


        return ls;

    }





    private List<List<Integer>> calculaMovimientoTorre(Piece piece) {
        List<List<Integer>> ls = new ArrayList<>();

        List<Integer> posicion = new ArrayList<>();

        int posXActual = piece.getXPosition();
        int posYActual = piece.getYPosition();

        //Vemos la vertical hacia arriba

        for(int posYNueva=posYActual-1;posYNueva>=0;posYNueva--){

            Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(posXActual, posYNueva,piece.getBoard().getId());

            if(piezaPosicion.isPresent()){
                if(piezaPosicion.get().color.equals(piece.getColor())){
                    break;
                }else if(!piezaPosicion.get().getType().equals("KING")){
                    posicion.add(posXActual);
                    posicion.add(posYNueva);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
                }else{
                    posicion.add(posXActual);
                    posicion.add(posYNueva);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();
                }
            }else{
                posicion.add(posXActual);
                posicion.add(posYNueva);
                ls.add(new ArrayList<>(posicion));
                posicion.clear();
            }
        }


        //Vemos la vertical hacia abajo

        for(int posYNueva=posYActual+1;posYNueva<=7;posYNueva++){

            Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(posXActual, posYNueva,piece.getBoard().getId());

            if(piezaPosicion.isPresent()){
                if(piezaPosicion.get().color.equals(piece.getColor())){
                    break;
                }else if(!piezaPosicion.get().getType().equals("KING")){
                    posicion.add(posXActual);
                    posicion.add(posYNueva);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
                }else{
                    posicion.add(posXActual);
                    posicion.add(posYNueva);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();
                }
            }else{
                posicion.add(posXActual);
                posicion.add(posYNueva);
                ls.add(new ArrayList<>(posicion));
                posicion.clear();
            }
        }



        
        //Vemos la horizontal hacia la izquierda

        for(int posXNueva=posXActual-1;posXNueva>=0;posXNueva--){

            Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(posXNueva, posYActual,piece.getBoard().getId());

            if(piezaPosicion.isPresent()){
                if(piezaPosicion.get().color.equals(piece.getColor())){
                    break;
                }else if(!piezaPosicion.get().getType().equals("KING")){
                    posicion.add(posXNueva);
                    posicion.add(posYActual);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
                }else{
                    posicion.add(posXNueva);
                    posicion.add(posYActual);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();
                }
            }else{
                posicion.add(posXNueva);
                posicion.add(posYActual);
                ls.add(new ArrayList<>(posicion));
                posicion.clear();
            }
        }


        //Vemos la horizontal hacia la derecha

        for(int posXNueva=posXActual+1;posXNueva<=7;posXNueva++){

            Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(posXNueva, posYActual,piece.getBoard().getId());

            if(piezaPosicion.isPresent()){
                if(piezaPosicion.get().color.equals(piece.getColor())){
                    break;
                }else if(!piezaPosicion.get().getType().equals("KING")){
                    posicion.add(posXNueva);
                    posicion.add(posYActual);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
                }else{
                    posicion.add(posXNueva);
                    posicion.add(posYActual);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();
                }
            }else{
                posicion.add(posXNueva);
                posicion.add(posYActual);
                ls.add(new ArrayList<>(posicion));
                posicion.clear();
            }
        }







        return ls;

    }


    private List<List<Integer>> calculaMovimientoCaballo(Piece piece) {
        List<List<Integer>> ls = new ArrayList<>();

        List<Integer> posicion = new ArrayList<>();

        for(int x=0;x<8;x++){
            for(int y = 0; y<8;y++){

                if(Math.abs(piece.getXPosition() - x) == 2 && Math.abs(piece.getYPosition() - y) == 1 || 
                Math.abs(piece.getXPosition() - x) == 1 && Math.abs(piece.getYPosition() - y) == 2){

                    Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(x, y,piece.getBoard().getId());

                    if(piezaPosicion.isPresent()){

                        if(piezaPosicion.get().color.equals(piece.getColor())){
                            continue;
                        }
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


    private List<List<Integer>> calculaMovimientoPeon(Piece piece){

        List<List<Integer>> ls = new ArrayList<>();

        List<Integer> posicion = new ArrayList<>();

        if(piece.getColor().equals("WHITE")){

            Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(piece.getXPosition(), piece.getYPosition()-1,piece.getBoard().getId());
            

            if(!piezaPosicion.isPresent() && piece.getYPosition()-1 >= 0){
                posicion.add(piece.getXPosition());
                posicion.add(piece.getYPosition()-1);
                ls.add(new ArrayList<>(posicion));

                if(piece.getYPosition()==6){
                    posicion.clear();
                    
                    Optional<Piece> piezaPosicion2 = this.pieceRepository.existePiezaPosicion(piece.getXPosition(), piece.getYPosition()-2,piece.getBoard().getId());

                    if(!piezaPosicion2.isPresent()){
                        posicion.add(piece.getXPosition());
                        posicion.add(piece.getYPosition()-2);
                        ls.add(new ArrayList<>(posicion));
                    }
                }


            }


            Optional<Piece> piezaPosicionDiagonal1 = this.pieceRepository.existePiezaPosicion(piece.getXPosition()-1, piece.getYPosition()-1,piece.getBoard().getId());

            if(piezaPosicionDiagonal1.isPresent() && !piezaPosicionDiagonal1.get().getColor().equals(piece.getColor())){
                posicion.clear();
                posicion.add(piece.getXPosition()-1);
                posicion.add(piece.getYPosition()-1);
                ls.add(new ArrayList<>(posicion));
            }


            Optional<Piece> piezaPosicionDiagonal2 = this.pieceRepository.existePiezaPosicion(piece.getXPosition()+1, piece.getYPosition()-1,piece.getBoard().getId());

            if(piezaPosicionDiagonal2.isPresent() && !piezaPosicionDiagonal2.get().getColor().equals(piece.getColor())){
                posicion.clear();
                posicion.add(piece.getXPosition()+1);
                posicion.add(piece.getYPosition()-1);
                ls.add(new ArrayList<>(posicion));
            }




            
        }else{

            Optional<Piece> piezaPosicion = this.pieceRepository.existePiezaPosicion(piece.getXPosition(), piece.getYPosition()+1,piece.getBoard().getId());


            if(!piezaPosicion.isPresent() && piece.getYPosition()+1 <= 7 ){
                posicion.add(piece.getXPosition());
                posicion.add(piece.getYPosition()+1);
                ls.add(new ArrayList<>(posicion));

               
            
                if(piece.getYPosition()==1){
                    posicion.clear(); 
                    
                    Optional<Piece> piezaPosicion2 = this.pieceRepository.existePiezaPosicion(piece.getXPosition(), piece.getYPosition()+2,piece.getBoard().getId());

                    if(!piezaPosicion2.isPresent()){

                        posicion.add(piece.getXPosition());
                        posicion.add(piece.getYPosition()+2);
                        ls.add(new ArrayList<>(posicion));
                    }
                }
            }


            Optional<Piece> piezaPosicionDiagonal1 = this.pieceRepository.existePiezaPosicion(piece.getXPosition()+1, piece.getYPosition()+1,piece.getBoard().getId());

            if(piezaPosicionDiagonal1.isPresent() && !piezaPosicionDiagonal1.get().getColor().equals(piece.getColor())){
                posicion.clear();
                posicion.add(piece.getXPosition()+1);
                posicion.add(piece.getYPosition()+1);
                ls.add(new ArrayList<>(posicion));
            }


            Optional<Piece> piezaPosicionDiagonal2 = this.pieceRepository.existePiezaPosicion(piece.getXPosition()-1, piece.getYPosition()+1,piece.getBoard().getId());

            if(piezaPosicionDiagonal2.isPresent() && !piezaPosicionDiagonal2.get().getColor().equals(piece.getColor())){
                posicion.clear();
                posicion.add(piece.getXPosition()-1);
                posicion.add(piece.getYPosition()+1);
                ls.add(new ArrayList<>(posicion));
            }
        }


        return ls;

        }

        



    



    
}
