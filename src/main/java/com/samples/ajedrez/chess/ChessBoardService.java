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


    public void savePiece(Piece piece){
        this.pieceRepository.save(piece);
    }




    public List<List<Integer>> listaMovimientos(Piece piece){


        List<List<Integer>> ls = new ArrayList<>();

        if(piece.getType().equals("PAWN")){

            ls =  calculaMovimientoPeon(piece);
            
        } else if(piece.getType().equals("HORSE")){

            ls = calculaMovimientoCaballo(piece);
        
        } else if(piece.getType().equals("TOWER")){
            ls = calculaMovimientoTorre(piece);
        }
        
        return ls;
    }





    private List<List<Integer>> calculaMovimientoTorre(Piece piece) {
        List<List<Integer>> ls = new ArrayList<>();

        List<Integer> posicion = new ArrayList<>();

        for(int x=0;x<8;x++){
            for(int y = 0; y<8;y++){

                if(piece.getXPosition() == x || piece.getYPosition() == y){
                    posicion.add(x);
                    posicion.add(y);
                    ls.add(new ArrayList<>(posicion));
                    posicion.clear();
                }
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

            if(piece.getYPosition()-1 >= 0 ){
                posicion.add(piece.getXPosition());
                posicion.add(piece.getYPosition()-1);
                ls.add(new ArrayList<>(posicion));

               
            
                if(piece.getYPosition()==6){
                    posicion.clear(); 

                    posicion.add(piece.getXPosition());
                    posicion.add(piece.getYPosition()-2);
                    ls.add(new ArrayList<>(posicion));
                    }
                }

            
        }else{
            if(piece.getYPosition()+1 <= 7 ){
                posicion.add(piece.getXPosition());
                posicion.add(piece.getYPosition()+1);
                ls.add(new ArrayList<>(posicion));

               
            
                if(piece.getYPosition()==1){
                    posicion.clear(); 

                    posicion.add(piece.getXPosition());
                    posicion.add(piece.getYPosition()+2);
                    ls.add(new ArrayList<>(posicion));
                    }
                }
            }


        return ls;

        }

        



    



    
}
