package com.samples.ajedrez.game;

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
            this.gameService.saveGame(game);

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
    public String joinPlayer(@PathVariable int gameId){
        Game game = this.gameService.findGameById(gameId);
        List<Player>participantes = game.getPlayer();

        Player player = this.gameService.jugadorSesion();

        if(player!=null && game.getNumeroJugadores()<2 && !participantes.contains(player)){
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
    public String estadoJugador(@PathVariable int gameId){
        Game game = this.gameService.findGameById(gameId);
        List<Player>participantes = game.getPlayer();

        Player player = this.gameService.jugadorSesion();

        if(participantes.contains(player)){
            return "jugador";
        }else{
            return "espectador";
        }

    }



    @GetMapping("/findGames")
    public List<Game> getGames(){
       
        
        List<Game> pl = this.gameService.findGames();

        return pl;

}





    
}
