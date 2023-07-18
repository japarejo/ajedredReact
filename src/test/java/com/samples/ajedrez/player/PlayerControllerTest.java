package com.samples.ajedrez.player;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.samples.ajedrez.user.User;

@SpringBootTest
public class PlayerControllerTest {

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    protected PlayerService playerService;

    public String obtenerToken() {

        String url = "http://localhost:8080/api/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        User loginRequest = new User("dani", "react");
        HttpEntity<User> requestEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response.getBody();
    }

    @Test
    public void testUpdatePlayer() {

        String url = "http://localhost:8080/api/player/update";
        String token = obtenerToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        String[] APELLIDOS = {
                "González", "Rodríguez", "López", "Martínez", "Pérez", "Gómez", "Sánchez", "Fernández", "Ramírez",
                "Torres"
        };

        Random random = new Random();
        int index = random.nextInt(APELLIDOS.length);

        String apellido = APELLIDOS[index];

        User user = new User("dani", "react");
        Player registerRequest = new Player("Daniel", apellido, "676876876", user);
        HttpEntity<Player> requestEntity = new HttpEntity<>(registerRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testErrorUpdatePlayer() {

        String url = "http://localhost:8080/api/player/update";
        String token = obtenerToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        User user = new User("adri", "react");
        Player registerRequest = new Player("Daniel", "Rodriguez", "676876876", user);
        HttpEntity<Player> requestEntity = new HttpEntity<>(registerRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
