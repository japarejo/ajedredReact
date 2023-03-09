package com.samples.ajedrez.game;

import java.sql.SQLException;
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


	private Boolean turno;

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


	public void updatePlayer(Player player){
		this.playerService.updatePlayer(player);
	}


	public void updateTurnPlayer(Player player){
		this.playerService.updateTurnPlayer(player);
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


	public void saveBoard(ChessBoard board){
		this.boardService.saveChessBoard(board);
	}


	public Boolean esJaque(String color, Piece pieza){
		return this.boardService.esJaque(color, pieza);
	}


	public Boolean esJaqueMate(String color, Piece pieza){
		return this.boardService.esJaqueMate(color, pieza);
	}

	public List<List<Integer>> listaMovimientos(Piece piece){

		return this.boardService.listaMovimientos(piece);
	}


	public void comprobarCasilla(int x, int y, int chessBoardId){
		this.boardService.comprobarCasilla(x, y, chessBoardId);
	}


	public void inicializacionTablero(ChessBoard board){

		this.boardService.saveChessBoard(board);

		Piece piece1 = new Piece();

		piece1.setColor("BLACK");
		piece1.setType("HORSE");
		piece1.setXPosition(1);
		piece1.setYPosition(0);
		piece1.setBoard(board);

		Piece piece2 = new Piece();

		piece2.setColor("WHITE");
		piece2.setType("QUEEN");
		piece2.setXPosition(4);
		piece2.setYPosition(7);
		piece2.setBoard(board);


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
		piece4.setYPosition(7);
		piece4.setBoard(board);

		Piece piece5 = new Piece();

		piece5.setColor("WHITE");
		piece5.setType("PAWN");
		piece5.setXPosition(4);
		piece5.setYPosition(6);
		piece5.setBoard(board);

		Piece piece6 = new Piece();

		piece6.setColor("WHITE");
		piece6.setType("TOWER");
		piece6.setXPosition(7);
		piece6.setYPosition(7);
		piece6.setBoard(board);

		Piece piece7 = new Piece();

		piece7.setColor("BLACK");
		piece7.setType("TOWER");
		piece7.setXPosition(0);
		piece7.setYPosition(0);
		piece7.setBoard(board);


		Piece piece8 = new Piece();

		piece8.setColor("BLACK");
		piece8.setType("BISHOP");
		piece8.setXPosition(5);
		piece8.setYPosition(0);
		piece8.setBoard(board);

		Piece piece9 = new Piece();

		piece9.setColor("BLACK");
		piece9.setType("KING");
		piece9.setXPosition(4);
		piece9.setYPosition(0);
		piece9.setBoard(board);


		this.boardService.savePiece(piece1);
		this.boardService.savePiece(piece2);
		this.boardService.savePiece(piece3);
		this.boardService.savePiece(piece4);
		this.boardService.savePiece(piece5);
		this.boardService.savePiece(piece6);
		this.boardService.savePiece(piece7);
		this.boardService.savePiece(piece8);
		this.boardService.savePiece(piece9);
	}

}