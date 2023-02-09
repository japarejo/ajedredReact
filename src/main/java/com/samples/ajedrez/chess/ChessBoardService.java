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


    public void comprobarCasilla(int x, int y, int chessBoardId){

        Optional<Piece> piezaComer = pieceRepository.existePiezaPosicion(x, y, chessBoardId);

        if(piezaComer.isPresent()){
            pieceRepository.remove(piezaComer.get().getId());

        }


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
                }else{
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
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
                }else{
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
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
                }else{
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
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
                }else{
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
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
                }else{
                    posicion.add(posXActual);
                    posicion.add(posYNueva);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
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
                }else{
                    posicion.add(posXActual);
                    posicion.add(posYNueva);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
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
                }else{
                    posicion.add(posXNueva);
                    posicion.add(posYActual);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
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
                }else{
                    posicion.add(posXNueva);
                    posicion.add(posYActual);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();

                    break;
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
