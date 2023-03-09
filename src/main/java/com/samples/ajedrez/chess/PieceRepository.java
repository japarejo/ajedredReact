package com.samples.ajedrez.chess;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface PieceRepository extends CrudRepository<Piece,Integer> {

    Piece findById(int id) throws DataAccessException;

    @Query("SELECT piece FROM Piece piece WHERE piece.xPosition = :x AND piece.yPosition = :y AND piece.board.id = :boardId")
	Optional<Piece> existePiezaPosicion(int x, int y,int boardId) throws DataAccessException;


    @Modifying
    @Query("DELETE FROM Piece piece WHERE piece.id = :pieceId")
    @Transactional
	public void remove(int pieceId) throws DataAccessException;



    @Query("SELECT piece FROM Piece piece WHERE piece.color = :color AND piece.board.id = :boardId")
    List<Piece> piezasJugador(String color, int boardId) throws DataAccessException;


    @Query("SELECT piece FROM Piece piece WHERE piece.color != :color AND piece.board.id = :boardId AND piece.type = 'KING'")
    Piece piezaReyContrario(String color, int boardId) throws DataAccessException;
    
}