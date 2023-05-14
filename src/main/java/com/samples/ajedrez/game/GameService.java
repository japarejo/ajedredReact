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


	public void comprobarCasilla(int x, int y, int chessBoardId,String color){
		this.boardService.comprobarCasilla(x, y, chessBoardId,color);
	}


	public Optional<Piece> piezaPosicion(int x, int y, int chessBoardId){
		return this.boardService.piezaPosicion(x, y, chessBoardId);
	}

	public Optional<Piece> peonSalto(int chessBoardId){
		return this.boardService.peonPaso(chessBoardId);
	}


	public void inicializacionTablero(ChessBoard board){

		this.boardService.inicializacionTablero(board);

	}


		
}