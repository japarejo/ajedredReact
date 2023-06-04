package com.samples.ajedrez.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.samples.ajedrez.chess.Piece;
import com.samples.ajedrez.player.Player;
import com.samples.ajedrez.player.PlayerService;
import com.samples.ajedrez.user.User;

import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
public class GameControllerTest {

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;




    public String obtenerToken(){

        String url = "http://localhost:8080/api/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        User loginRequest = new User("dani", "react");
        HttpEntity<User> requestEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response.getBody();
    }


    public String obtenerToken2(){

        String url = "http://localhost:8080/api/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        User loginRequest = new User("adri", "react");
        HttpEntity<User> requestEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response.getBody();
    }


    @BeforeEach
    void setup(){

        String url = "http://localhost:8080/api/games/create";
        String token = obtenerToken2();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Game createRequest = new Game(1,"Partida1",10,true);

        HttpEntity<Game> requestEntity = new HttpEntity<>(createRequest, headers);

        restTemplate.postForEntity(url, requestEntity, String.class);

        Game game = this.gameService.findGameById(1);

        Player player = game.getPlayer().get(0);

        Instant instant =Instant.now();

        player.setInicioTurno(instant);

        this.playerService.updateTurnPlayer(player);

    }


    @Test
    public void testCreateGame() {
        
        String url = "http://localhost:8080/api/games/create";
        String token = obtenerToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Game createRequest = new Game(2,"PartidaPrueba",10,true);

        HttpEntity<Game> requestEntity = new HttpEntity<>(createRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testJoinPlayer() {
        
        String url = "http://localhost:8080/api/games/1/join";
        String token = obtenerToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testJoinEspectador() {
        
        String url = "http://localhost:8080/api/games/1/espectador";
        String token = obtenerToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        Game game = gameService.findGameById(1);

        int nPlayers = game.getNumeroJugadores();

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(game.getNumeroJugadores(),nPlayers);
    }


    @Test
    @WithMockUser(username = "dani", roles = "Player")
    public void testListaMovimientos() {
        
        String url = "http://localhost:8080/api/games/listMovements/1";
        String token = obtenerToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        Player player = this.gameService.jugadorSesion();

        player.setTime(5);

        player.setInicioTurno(Instant.now());

        this.playerService.updatePlayer(player);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @WithMockUser(username = "dani", roles = "Player")
    public void testMovimiento() {
        
        String url = "http://localhost:8080/api/games/1/move";
        String token = obtenerToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        Player player = this.gameService.jugadorSesion();

        player.setTime(5);

        player.setInicioTurno(Instant.now());

        player.setColorPartida("WHITE");

        this.playerService.updatePlayer(player);

        Game game = this.gameService.findGameById(1);
        List<Player>players = game.getPlayer();
        players.add(player);

        game.setPlayer(players);

        this.gameService.saveGame(game);

        Piece piece = this.gameService.findPieceById(1);

        int xPosition = piece.getXPosition();

        Piece createRequest = new Piece(1,5,4);

        HttpEntity<Piece> requestEntity = new HttpEntity<>(createRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        int newXPosition = this.gameService.findPieceById(1).getXPosition();

        assertNotEquals(xPosition,newXPosition);
    }

    
}
