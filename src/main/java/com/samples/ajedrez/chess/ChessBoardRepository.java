package com.samples.ajedrez.chess;

import org.springframework.data.repository.CrudRepository;

public interface ChessBoardRepository extends CrudRepository<ChessBoard,Integer> {
    
}
