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


	public Boolean esJaque(String color, Piece pieza, int[][] tablero){
		return this.boardService.esJaque(color, pieza,tablero);
	}


	public Boolean esJaqueMate(String color, Piece pieza, int[][] tablero){
		return this.boardService.esJaqueMate(color, pieza, tablero);
	}

	public List<List<Integer>> listaMovimientos(Piece piece, int[][] tablero){

		return this.boardService.listaMovimientos(piece,tablero);
	}


	public void comprobarCasilla(int x, int y, int chessBoardId){
		this.boardService.comprobarCasilla(x, y, chessBoardId);
	}


	public Optional<Piece> piezaPosicion(int x, int y, int chessBoardId){
		return this.boardService.piezaPosicion(x, y, chessBoardId);
	}

	public Optional<Piece> peonSalto(int chessBoardId){
		return this.boardService.peonPaso(chessBoardId);
	}


	public void inicializacionTablero(ChessBoard board){

		this.boardService.saveChessBoard(board);


		for(int i=0; i<8;i++){
			Piece piece = new Piece();

			piece.setColor("WHITE");
			piece.setType("PAWN");
			piece.setXPosition(i);
			piece.setYPosition(6);
			piece.setBoard(board);
			piece.setPiezaMovida(false);
			piece.setPeonPaso(false);
			this.boardService.savePiece(piece);
		}


		for(int i=0; i<8;i++){
			Piece piece = new Piece();

			piece.setColor("BLACK");
			piece.setType("PAWN");
			piece.setXPosition(i);
			piece.setYPosition(1);
			piece.setBoard(board);
			piece.setPiezaMovida(false);
			piece.setPeonPaso(false);
			this.boardService.savePiece(piece);
		}


		Piece piece1 = new Piece();
		piece1.setColor("WHITE");
		piece1.setType("KING");
		piece1.setXPosition(4);
		piece1.setYPosition(7);
		piece1.setBoard(board);
		piece1.setPiezaMovida(false);
		piece1.setPeonPaso(false);


		Piece piece2 = new Piece();
		piece2.setColor("BLACK");
		piece2.setType("KING");
		piece2.setXPosition(4);
		piece2.setYPosition(0);
		piece2.setBoard(board);
		piece2.setPiezaMovida(false);
		piece2.setPeonPaso(false);

		Piece piece3 = new Piece();
		piece3.setColor("WHITE");
		piece3.setType("QUEEN");
		piece3.setXPosition(3);
		piece3.setYPosition(7);
		piece3.setBoard(board);
		piece3.setPiezaMovida(false);
		piece3.setPeonPaso(false);


		Piece piece4 = new Piece();
		piece4.setColor("BLACK");
		piece4.setType("QUEEN");
		piece4.setXPosition(3);
		piece4.setYPosition(0);
		piece4.setBoard(board);
		piece4.setPiezaMovida(false);
		piece4.setPeonPaso(false);


		for(int i=2;i<6;i=i+3){
			Piece piece = new Piece();
			piece.setColor("BLACK");
			piece.setType("BISHOP");
			piece.setXPosition(i);
			piece.setYPosition(0);
			piece.setBoard(board);
			piece.setPiezaMovida(false);
			piece.setPeonPaso(false);
			this.boardService.savePiece(piece);
		}

		for(int i=2;i<6;i=i+3){
			Piece piece = new Piece();
			piece.setColor("WHITE");
			piece.setType("BISHOP");
			piece.setXPosition(i);
			piece.setYPosition(7);
			piece.setBoard(board);
			piece.setPiezaMovida(false);
			piece.setPeonPaso(false);
			this.boardService.savePiece(piece);
		}

		for(int i=1;i<7;i=i+5){
			Piece piece = new Piece();

			piece.setColor("BLACK");
			piece.setType("HORSE");
			piece.setXPosition(i);
			piece.setYPosition(0);
			piece.setBoard(board);
			piece.setPiezaMovida(false);
			piece.setPeonPaso(false);
			this.boardService.savePiece(piece);
		}

		for(int i=1;i<7;i=i+5){
			Piece piece = new Piece();

			piece.setColor("WHITE");
			piece.setType("HORSE");
			piece.setXPosition(i);
			piece.setYPosition(7);
			piece.setBoard(board);
			piece.setPiezaMovida(false);
			piece.setPeonPaso(false);
			this.boardService.savePiece(piece);
		}
		


		for(int i=0;i<8;i=i+7){
			Piece piece = new Piece();

			piece.setColor("WHITE");
			piece.setType("TOWER");
			piece.setXPosition(i);
			piece.setYPosition(7);
			piece.setBoard(board);
			piece.setPiezaMovida(false);
			piece.setPeonPaso(false);
			this.boardService.savePiece(piece);
		}
		

		for(int i=0;i<8;i=i+7){
			Piece piece = new Piece();

			piece.setColor("BLACK");
			piece.setType("TOWER");
			piece.setXPosition(i);
			piece.setYPosition(0);
			piece.setBoard(board);
			piece.setPiezaMovida(false);
			piece.setPeonPaso(false);
			this.boardService.savePiece(piece);
		}

		this.boardService.savePiece(piece1);
		this.boardService.savePiece(piece2);
		this.boardService.savePiece(piece3);
		this.boardService.savePiece(piece4);
	}

}