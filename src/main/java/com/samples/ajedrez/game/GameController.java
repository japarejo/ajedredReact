package com.samples.ajedrez.game;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samples.ajedrez.chess.ChessBoard;
import com.samples.ajedrez.chess.Piece;
import com.samples.ajedrez.player.Player;

@RequestMapping("/games")
@RestController
public class GameController {

    private final GameService gameService;



	@Autowired
	public GameController(GameService gameService) {
		this.gameService = gameService;
		
	}


    @InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


    @PostMapping("/create")
    public String createGame(@RequestBody Game game){

        List<Player>participantes = new ArrayList<>();
        Player player = this.gameService.jugadorSesion();

        if(player!=null){
            participantes.add(player);
            game.setPlayer(participantes);
            game.setNumeroJugadores(1);

            ChessBoard board = new ChessBoard();

            board.setTurn("WHITE");

            board.setJaque(false);

            this.gameService.inicializacionTablero(board);

            game.setChessBoard(board);

            game.setFinPartida(false);

            this.gameService.saveGame(game);

            int numero = (int) (Math.random() * 2);


            if(numero == 0){

                player.setColorPartida("WHITE");

            }else{
                player.setColorPartida("BLACK");

            }

            player.setTime(game.getTiempo()*60);


            this.gameService.updatePlayer(player);

            

            return game.getId().toString();
        
        }else{
            return "Error";
        }
        
    }


    @GetMapping("/{gameId}/awaitGame")
    public Integer awaitGame(@PathVariable int gameId){

        Game game = this.gameService.findGameById(gameId);

        return game.getPlayer().size();
    }

    @GetMapping("/{gameId}/join")
    public String joinPlayer(@PathVariable int gameId) throws SQLException{
        Game game = this.gameService.findGameById(gameId);
        List<Player>participantes = game.getPlayer();

        Player player = this.gameService.jugadorSesion();

        if(player!=null && game.getNumeroJugadores()<2 && !participantes.contains(player)){

            String colorJugadorUnido = participantes.get(0).getColorPartida();


            if(colorJugadorUnido.equals("WHITE")){
                player.setColorPartida("BLACK");
            }else{
                player.setColorPartida("WHITE");
            }


            player.setTime(game.getTiempo()*60);
            
            this.gameService.updatePlayer(player);


            participantes.add(player);

            game.setPlayer(participantes);

            game.setNumeroJugadores(game.getNumeroJugadores()+1);

            this.gameService.saveGame(game);

            return "OK";
        
        }else{
            return "Error";
        }
    }


    @GetMapping("/{gameId}")
    public List<Object> inicioPartida(@PathVariable int gameId){

        List<Object> partida = new ArrayList<>();

        Game game = this.gameService.findGameById(gameId);

        ChessBoard tablero = game.getChessBoard();

        partida.add(tablero);

        String colorJugador = this.gameService.jugadorSesion().getColorPartida();
        partida.add(colorJugador);

        Player player = this.gameService.jugadorSesion();

        partida.add(player.getTime());

        partida.add(game.getFinPartida());

        return partida;

    }


    @GetMapping("/{gameId}/startTurn")
    public String inicioTurno(@PathVariable int gameId){

        Instant inicio = Instant.now();

        Player player = this.gameService.jugadorSesion();

        if(player.getInicioTurno() == null){

            Game game = this.gameService.findGameById(gameId);

            for(Player jugador: game.getPlayer()){

                if(!jugador.equals(player)){
                    jugador.setInicioTurno(null);
                    this.gameService.updateTurnPlayer(jugador);
                }
            }

            player.setInicioTurno(inicio);

            this.gameService.updateTurnPlayer(player);
        }

        return "OK";
    }


    
    



    @PostMapping("/listMovements")
    public List<List<Integer>> listaMovimientos(@RequestBody Piece piece){

        Piece pieza = this.gameService.findPieceById(piece.getId());

    
        return this.gameService.listaMovimientos(pieza);



    }


    @PostMapping("/{gameId}/move")
    public List<Object> movimiento(@PathVariable int gameId,@RequestBody Piece piece) throws InterruptedException{

        

        Piece pieza = this.gameService.findPieceById(piece.getId());

        Integer posX = piece.getXPosition();
        System.out.println(posX);
        Integer posY = piece.getYPosition();

        List<List<Integer>> listaMovimientos = listaMovimientos(piece);

        List<Integer> movimiento = new ArrayList<>();

        movimiento.add(posX);
        movimiento.add(posY);

        if(listaMovimientos.contains(movimiento)){

            Player player = this.gameService.jugadorSesion();
            
            Instant finTurno = Instant.now();

            Instant inicioTurno = player.getInicioTurno();

            Duration duracion = Duration.between(inicioTurno, finTurno);

            int segundos = (int) duracion.getSeconds();


            int tiempoRestante = player.getTime()-segundos>0? player.getTime()-segundos : 0;

            player.setTime(tiempoRestante);

            this.gameService.updatePlayer(player);

            this.gameService.comprobarCasilla(posX, posY, pieza.getBoard().getId());
            pieza.setXPosition(posX);
            pieza.setYPosition(posY);

            this.gameService.savePiece(pieza);

            ChessBoard tablero = pieza.getBoard();

            Game game = this.gameService.findGameById(gameId);

            Boolean esJaque = this.gameService.esJaque(player.getColorPartida(), pieza);

            tablero.setJaque(esJaque);

            if(esJaque){
                Boolean esJaqueMate = this.gameService.esJaqueMate(player.getColorPartida(), pieza);

                if(esJaqueMate){
                    
                    game.setFinPartida(true);

                    this.gameService.saveGame(game);
                }
            }

            if(tablero.getTurn().equals("WHITE")){
                tablero.setTurn("BLACK");
            
            }else{
                tablero.setTurn("WHITE");
            }

            this.gameService.saveBoard(tablero);

            List<Object> partida = new ArrayList<>();

            partida.add(tablero);

            partida.add(game.getFinPartida());

            return partida;

        
        } else{

            ChessBoard tablero = pieza.getBoard();
            List<Object> partida = new ArrayList<>();

            partida.add(tablero);

            return partida;

        }

    }



    



    @GetMapping("/findGames")
    public List<Game> getGames(){
       
        
        List<Game> pl = this.gameService.findGames();

        return pl;

}






    
}
