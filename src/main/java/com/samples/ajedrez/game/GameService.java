package com.samples.ajedrez.game;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samples.ajedrez.chess.ChessBoard;
import com.samples.ajedrez.chess.ChessBoardService;
import com.samples.ajedrez.chess.Piece;
import com.samples.ajedrez.player.Player;
import com.samples.ajedrez.player.PlayerService;
import com.samples.ajedrez.user.UserService;

@Service
public class GameService {

    private GameRepository gameRepository;
	private PlayerService playerService;
	private UserService userService;
	private ChessBoardService boardService;

    @Autowired
	public GameService(GameRepository gameRepository, PlayerService playerService, UserService userService,ChessBoardService boardService) {
		this.gameRepository = gameRepository;
		this.playerService = playerService;
		this.userService = userService;
		this.boardService = boardService;
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


	public Optional<ChessBoard> findBoardById(Integer id){
		return this.boardService.findById(id);
	}



	public Piece findPieceById(Integer id){
		return this.boardService.findPieceById(id);
	}



	public void savePiece(Piece piece){
		this.boardService.savePiece(piece);
	}



	public List<List<Integer>> listaMovimientos(Piece piece){

		return this.boardService.listaMovimientos(piece);
	}

}