package com.samples.ajedrez.game;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samples.ajedrez.player.Player;
import com.samples.ajedrez.player.PlayerService;
import com.samples.ajedrez.user.UserService;

@Service
public class GameService {

    private GameRepository gameRepository;
	private PlayerService playerService;
	private UserService userService;

    @Autowired
	public GameService(GameRepository gameRepository, PlayerService playerService, UserService userService) {
		this.gameRepository = gameRepository;
		this.playerService = playerService;
		this.userService = userService;
    }
	
	@Transactional(readOnly = true)
	public List<Game> findGames() throws DataAccessException {
		return gameRepository.findGames();
	}


    public void saveGame(Game game) {
		this.gameRepository.save(game);
	}


    @Transactional(readOnly = true)
	public Game findGameById(int id) throws DataAccessException {
		return gameRepository.findById(id);
	}


	public Player jugadorSesion(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return this.playerService.findPlayerByUsername(this.userService.findUser(username).orElse(null));
	}

}