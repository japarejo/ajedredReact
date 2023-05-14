package com.samples.ajedrez.game;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends CrudRepository<Game, Integer> {

	@Query("SELECT game FROM Game game WHERE game.numeroJugadores < 2 OR game.espectadores=True ORDER BY game.name")
	List<Game> findGames() throws DataAccessException;

	@Query("SELECT COUNT(*) FROM Game")
	int totalPartidas() throws DataAccessException;

	Game findById(int id) throws DataAccessException;
}
