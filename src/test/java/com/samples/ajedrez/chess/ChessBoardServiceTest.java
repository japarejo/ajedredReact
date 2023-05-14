package com.samples.ajedrez.chess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ChessBoardServiceTest {

    @Autowired
    private ChessBoardService boardService;


    @BeforeEach
    void setup(){
        ChessBoard board = new ChessBoard();
        board.setTurn("WHITE");
        board.setJaque(false);

        board.setJaqueMate(false);

        board.setCoronacion(false);
        this.boardService.saveChessBoard(board);

        this.boardService.inicializacionTablero(board);
    }

    @Test
    public void testFindBoardById() {

        ChessBoard board = new ChessBoard();
        board.setTurn("BLACK");
        this.boardService.saveChessBoard(board);

        ChessBoard g = this.boardService.findById(1).get();
		
        assertThat(g.getTurn().equals("BLACK"));
        
    }

    @Test
    public void testFindPieceById() {

        Piece p = this.boardService.findPieceById(1);
		
        assertThat(p.getType().equals("PAWN"));
        
    }


    @Test
    public void testComprobarCasilla() {

        Optional<Piece> p = this.boardService.piezaPosicion(1, 6, 1);

        assertThat(p.isPresent()).isTrue();

        this.boardService.comprobarCasilla(1, 6, 1, "BLACK");

        Optional<Piece> p2 = this.boardService.piezaPosicion(1, 6, 1);

        assertThat(p2.isEmpty()).isTrue();
        
    }



    
    
}
