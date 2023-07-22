package com.samples.ajedrez.game;

import com.samples.ajedrez.chess.ChessBoard;
import com.samples.ajedrez.chess.ChessBoardService;
import com.samples.ajedrez.chess.Piece;
import com.samples.ajedrez.player.Player;
import com.samples.ajedrez.player.PlayerService;
import com.samples.ajedrez.user.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final ChessBoardService boardService;

    @Autowired
    public GameService(GameRepository gameRepository,
            PlayerService playerService, UserService userService,
            ChessBoardService boardService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.boardService = boardService;
    }

    @Transactional(readOnly = true)
    public List<Game> findGames() throws DataAccessException {
        return gameRepository.findGames();
    }

    @Transactional
    public void saveGame(Game game) {
        this.gameRepository.save(game);
    }

    @Transactional(readOnly = true)
    public Game findGameById(int id) throws DataAccessException {
        return gameRepository.findById(id);
    }

    public Player jugadorSesion() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return this.playerService.findPlayerByUsername(username);
    }

    @Transactional
    public void updatePlayer(Player player) {
        this.playerService.updatePlayer(player);
    }

    @Transactional
    public void updateTurnPlayer(Player player) {
        this.playerService.updateTurnPlayer(player);
    }

    @Transactional(readOnly = true)
    public Optional<ChessBoard> findBoardById(Integer id) {
        return this.boardService.findById(id);
    }

    @Transactional(readOnly = true)
    public Piece findPieceById(Integer id) {
        return this.boardService.findPieceById(id);
    }

    @Transactional
    public void savePiece(Piece piece) {
        this.boardService.savePiece(piece);
    }
    
    @Transactional
    public void saveBoard(ChessBoard board) {
        this.boardService.saveChessBoard(board);
    }

    public Boolean esJaque(String color, Piece pieza, int[][] tablero) {
        return this.boardService.esJaque(color, pieza, tablero);
    }

    public Boolean esJaqueMate(String color, Piece pieza, int[][] tablero) {
        return this.boardService.esJaqueMate(color, pieza, tablero);
    }

    public List<List<Integer>> listaMovimientos(Piece piece, int[][] tablero) {

        return this.boardService.listaMovimientos(piece, tablero);
    }

    public void comprobarCasilla(int x, int y, int chessBoardId, String color) {
        this.boardService.comprobarCasilla(x, y, chessBoardId, color);
    }

    public Optional<Piece> piezaPosicion(int x, int y, int chessBoardId) {
        return this.boardService.piezaPosicion(x, y, chessBoardId);
    }

    public Optional<Piece> peonSalto(int chessBoardId) {
        return this.boardService.peonPaso(chessBoardId);
    }

    public void inicializacionTablero(ChessBoard board) {

        this.boardService.inicializacionTablero(board);

    }
    
    @Transactional(readOnly = true)
    public int getGamesPlayedByPlayerUsername(String username) {

        return this.gameRepository.countGamesPlayedByPlayer(username);   
    }

}
