package com.samples.ajedrez.chess;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChessBoardRepository extends CrudRepository<ChessBoard,Integer> {

    @Query("SELECT board FROM ChessBoard board")
    List<ChessBoard> totalTableros() throws DataAccessException;
    
}
