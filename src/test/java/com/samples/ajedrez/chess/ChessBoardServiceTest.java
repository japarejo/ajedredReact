package com.samples.ajedrez.chess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

@SpringBootTest
public class ChessBoardServiceTest {

    @Autowired
    private ChessBoardService boardService;

    @BeforeEach
    void setup() {
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

        ChessBoard g = this.boardService.findById(this.boardService.totalChessBoards().size()).get();

        assertEquals(g.getTurn(), "BLACK");

    }

    @Test
    public void testFindPieceById() {

        Piece p = this.boardService.findPieceById(1);

        assertEquals(p.getType(), "PAWN");

    }

    @Test
    public void testComprobarCasilla() {

        Optional<Piece> p = this.boardService.piezaPosicion(1, 6, 1);

        assertThat(p.isPresent()).isTrue();

        this.boardService.comprobarCasilla(1, 6, 1, "BLACK");

        Optional<Piece> p2 = this.boardService.piezaPosicion(1, 6, 1);

        assertThat(p2.isEmpty()).isTrue();

    }

    @Test
    public void testJaque() {

        Piece pieza = this.boardService.findPieceById(1);

        int[][] tablero = new int[8][8];

        for (int i = 2; i <= 32; i++) {
            Piece p = this.boardService.findPieceById(i);

            if (p.getColor().equals("WHITE")) {
                tablero[p.getXPosition()][p.getYPosition()] = 10;
            } else {
                tablero[p.getXPosition()][p.getYPosition()] = 11;
            }
        }

        Boolean esJaque = this.boardService.esJaque("WHITE", pieza, tablero);

        assertEquals(esJaque, false);

    }

}
