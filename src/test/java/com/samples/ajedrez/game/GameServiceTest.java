package com.samples.ajedrez.game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    public void testFindGameById() {

        Game game = new Game();
        game.setName("Partida Servicio");
        game.setTiempo(3);
        game.setEspectadores(true);
        game.setNumeroJugadores(1);
        this.gameService.saveGame(game);

        Game g = this.gameService.findGameById(this.gameService.findGames().size());
		
        assertEquals(g.getName(),"Partida Servicio");
        
    }


    @Test
    public void testFindGames() {

        Game game = new Game();
        game.setName("Partida Servicio2");
        game.setTiempo(3);
        game.setEspectadores(true);
        game.setNumeroJugadores(1);
        this.gameService.saveGame(game);

        Game game2 = new Game();
        game2.setName("Partida Servicio3");
        game2.setTiempo(3);
        game2.setEspectadores(false);
        game2.setNumeroJugadores(1);
        this.gameService.saveGame(game2);

        int totalPartidas = this.gameService.findGames().size();
		
        assertTrue(totalPartidas>=2);
        
    }
    
}
