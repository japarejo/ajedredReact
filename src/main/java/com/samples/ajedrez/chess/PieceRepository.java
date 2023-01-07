package com.samples.ajedrez.chess;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface PieceRepository extends CrudRepository<Piece,Integer> {

    Piece findById(int id) throws DataAccessException;

    @Query("SELECT piece FROM Piece piece WHERE piece.xPosition = :x AND piece.yPosition = :y")
	Optional<Piece> existePiezaPosicion(int x, int y) throws DataAccessException;
    
}