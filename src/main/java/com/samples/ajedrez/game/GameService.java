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


	public void savePlayer(Player player){
		this.playerService.savePlayer(player);
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


	public void inicializacionTablero(ChessBoard board){

		this.boardService.saveChessBoard(board);

		Piece piece1 = new Piece();

		piece1.setColor("BLACK");
		piece1.setType("HORSE");
		piece1.setXPosition(0);
		piece1.setYPosition(2);
		piece1.setBoard(board);

		Piece piece2 = new Piece();

		piece2.setColor("WHITE");
		piece2.setType("QUEEN");
		piece2.setXPosition(4);
		piece2.setYPosition(4);
		piece2.setBoard(board);

		this.boardService.savePiece(piece1);
		this.boardService.savePiece(piece2);


		Piece piece3 = new Piece();

		piece3.setColor("BLACK");
		piece3.setType("PAWN");
		piece3.setXPosition(1);
		piece3.setYPosition(1);
		piece3.setBoard(board);


		Piece piece4 = new Piece();

		piece4.setColor("WHITE");
		piece4.setType("KING");
		piece4.setXPosition(3);
		piece4.setYPosition(5);
		piece4.setBoard(board);

		Piece piece5 = new Piece();

		piece5.setColor("WHITE");
		piece5.setType("PAWN");
		piece5.setXPosition(4);
		piece5.setYPosition(6);
		piece5.setBoard(board);

		this.boardService.savePiece(piece1);
		this.boardService.savePiece(piece2);
		this.boardService.savePiece(piece3);
		this.boardService.savePiece(piece4);
		this.boardService.savePiece(piece5);

	}

}