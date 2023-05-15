package com.samples.ajedrez.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.samples.ajedrez.player.Player;

@SpringBootTest
class UserControllerTest {

    
    private TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testLoginSuccess() {
        
        String url = "http://localhost:8080/api/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        User loginRequest = new User("dani", "react");
        HttpEntity<User> requestEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
	public void testLoginError() {
        
        String url = "http://localhost:8080/api/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        User loginRequest = new User("username", "password");
        HttpEntity<User> requestEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }



    @Test
	public void testRegisterSuccess() {
        
        String url = "http://localhost:8080/api/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        User user = new User("alba", "1234password");
        Player registerRequest = new Player("Alba","Rodriguez","676876876",user);
        HttpEntity<Player> requestEntity = new HttpEntity<>(registerRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());


    }

    @Test
	public void testRegisterError() {
        
        String url = "http://localhost:8080/api/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        User user = new User("dani", "react");
        Player registerRequest = new Player("Daniel","Rodriguez","676876876",user);
        HttpEntity<Player> requestEntity = new HttpEntity<>(registerRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());


    }
    
    
	

}