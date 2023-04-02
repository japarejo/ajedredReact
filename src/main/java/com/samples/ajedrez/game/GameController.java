package com.samples.ajedrez.game;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

@RequestMapping("/api/games")
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

            board.setJaqueMate(false);

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

            player.setInicioTurno(null);


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

            player.setInicioTurno(null);
            
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

        Player jugadorRival = game.getPlayer().stream().filter(x-> !x.getUser().equals(player.getUser())).findAny().orElse(null);

        partida.add(player.getTime());

        partida.add(game.getFinPartida());

        partida.add(jugadorRival.getTime());

        return partida;

    }


    @GetMapping("/{gameId}/startTurn")
    public String inicioTurno(@PathVariable int gameId){

        Instant inicio = Instant.now();

        Player player = this.gameService.jugadorSesion();

        if(player.getInicioTurno() == null){
            player.setInicioTurno(inicio);

            this.gameService.updateTurnPlayer(player);

           
        }

        Game game = this.gameService.findGameById(gameId);

        for(Player jugador: game.getPlayer()){

            if(!jugador.equals(player)){
                jugador.setInicioTurno(null);
                this.gameService.updateTurnPlayer(jugador);
            }
        }

        return "OK";
    }




    @GetMapping("/{gameId}/endTime")
    public List<Object> comprobarFinTiempo(@PathVariable int gameId){

        Instant instanteActual = Instant.now();

        Player player = this.gameService.jugadorSesion();

        Game game = this.gameService.findGameById(gameId);

        Instant inicioTurno = player.getInicioTurno();
        Duration duracion = Duration.between(inicioTurno, instanteActual);
        int segundos = (int) duracion.getSeconds();
        int tiempoRestante = player.getTime()-segundos>0? player.getTime()-segundos : 0;

        ChessBoard tablero = game.getChessBoard();

        if(tiempoRestante == 0){
            game.setFinPartida(true);

            this.gameService.saveGame(game);

            

            if(tablero.getTurn().equals("WHITE")){
                tablero.setTurn("BLACK");
            
            }else{
                tablero.setTurn("WHITE");
            }
    
            this.gameService.saveBoard(tablero);
            
        }

        player.setTime(tiempoRestante);

        this.gameService.updatePlayer(player);

        List<Object> partida = new ArrayList<>();

        partida.add(tablero);

        partida.add(game.getFinPartida());

        partida.add(player.getTime());


        return partida;

        
    }


    
    



    @PostMapping("/listMovements")
    public List<List<Integer>> listaMovimientos(@RequestBody Piece piece){

        Piece pieza = this.gameService.findPieceById(piece.getId());

        //Si es blanca, metemos 10, si es negra 11. Si no hay, sera 0
        int[][] tablero = new int[8][8];

        for(Piece p: pieza.getBoard().getPieces()){
            
            if(p.getColor().equals("WHITE")){
                tablero[p.getXPosition()][p.getYPosition()] = 10;
            }else{
                tablero[p.getXPosition()][p.getYPosition()] = 11;
            }
            
        }
        

    
        return this.gameService.listaMovimientos(pieza,tablero);



    }


    @PostMapping("/{gameId}/move")
    public List<Object> movimiento(@PathVariable int gameId,@RequestBody Piece piece) throws InterruptedException{

        

        Piece pieza = this.gameService.findPieceById(piece.getId());

        Integer posX = piece.getXPosition();
        Integer posY = piece.getYPosition();

        int indiceY = pieza.getColor().equals("WHITE")? 7: 0 ;

        List<Integer> movimiento = new ArrayList<>();

        movimiento.add(posX);
        movimiento.add(posY);

        

        Player player = this.gameService.jugadorSesion();
            
        Instant finTurno = Instant.now();

        Instant inicioTurno = player.getInicioTurno();

        Duration duracion = Duration.between(inicioTurno, finTurno);

        int segundos = (int) duracion.getSeconds();


        int tiempoRestante = player.getTime()-segundos>0? player.getTime()-segundos : 0;

        player.setTime(tiempoRestante);

        this.gameService.updatePlayer(player);

        Boolean esFinPartidaTiempo = false;

        if(tiempoRestante == 0){
            esFinPartidaTiempo = true;
        }

        this.gameService.comprobarCasilla(posX, posY, pieza.getBoard().getId());

        if(!pieza.getPiezaMovida()){

            if(pieza.getType().equals("KING") && Math.abs(posX - pieza.getXPosition()) == 2){
                if(posX > pieza.getXPosition()){ // Enroque corto
                    Optional<Piece> piezaTorreEnroque = this.gameService.piezaPosicion(7, indiceY, pieza.getBoard().getId());

                    piezaTorreEnroque.get().setXPosition(5);
                    piezaTorreEnroque.get().setYPosition(indiceY);
                    piezaTorreEnroque.get().setPiezaMovida(true);

                    this.gameService.savePiece(piezaTorreEnroque.get());
                
                }else{ // Enroque largo
                    Optional<Piece> piezaTorreEnroque = this.gameService.piezaPosicion(0, indiceY, pieza.getBoard().getId());

                    piezaTorreEnroque.get().setXPosition(3);
                    piezaTorreEnroque.get().setYPosition(indiceY);
                    piezaTorreEnroque.get().setPiezaMovida(true);

                    this.gameService.savePiece(piezaTorreEnroque.get());
                }
            }
        }

        pieza.setPiezaMovida(true);
        pieza.setXPosition(posX);
        pieza.setYPosition(posY);

        this.gameService.savePiece(pieza);

        ChessBoard board = pieza.getBoard();

        if(board.getTurn().equals("WHITE")){
            board.setTurn("BLACK");
                
        }else{
            board.setTurn("WHITE");
        }
        

        this.gameService.saveBoard(board);



        Game game = this.gameService.findGameById(gameId);

        if(esFinPartidaTiempo){
            game.setFinPartida(true);
        }

       if(!esFinPartidaTiempo){

            int[][] tablero = new int[8][8];

            for(Piece p: pieza.getBoard().getPieces()){
                
                if(p.getColor().equals("WHITE")){
                    tablero[p.getXPosition()][p.getYPosition()] = 10;
                }else{
                    tablero[p.getXPosition()][p.getYPosition()] = 11;
                }
                
            }


            Boolean esJaque = this.gameService.esJaque(player.getColorPartida(), pieza,tablero);
                
            Boolean esJaqueMate = false;

            board.setJaque(esJaque);

        
            esJaqueMate = this.gameService.esJaqueMate(player.getColorPartida(), pieza, tablero);

            board.setJaqueMate(esJaqueMate);

            if(esJaqueMate){
                game.setFinPartida(true); // Si es jaque mate y no es jaque, es estancamiento(rey ahogado)
            }

            
        }

        this.gameService.saveBoard(board);

        this.gameService.saveGame(game);


        List<Object> partida = new ArrayList<>();

        partida.add(board);

        partida.add(game.getFinPartida());

        partida.add(player.getTime());
            
        Player jugadorRival = game.getPlayer().stream().filter(x-> !x.getUser().equals(player.getUser())).findAny().orElse(null);

        partida.add(jugadorRival.getTime());

        return partida;

        

    }



    



    @GetMapping("/findGames")
    public List<Game> getGames(){
       
        
        List<Game> pl = this.gameService.findGames();

        return pl;

}






    
}
