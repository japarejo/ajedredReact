package com.samples.ajedrez.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samples.ajedrez.player.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginSuccess() throws Exception {

        String username = "test";
        String password = "test";
        User user = new User(username, password);
        String loginRequest = objectMapper.writeValueAsString(user);
        this.mvc.perform(post("/api/login").content(loginRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginError() throws Exception {

        String username = "$error$";
        String password = "error";
        User user = new User(username, password);
        String loginRequest = objectMapper.writeValueAsString(user);
        this.mvc.perform(post("/api/login").content(loginRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRegisterSuccess() throws Exception {

        User user = new User("john", "johndoe");
        Player player = new Player("John", "Doe", "123456789", user);

        String registerRequest = objectMapper.writeValueAsString(player);
        this.mvc.perform(post("/api/register").content(registerRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void testRegisterAccountAlreadyExists() throws Exception {

        User user = new User("test", "test");
        Player player = new Player("John", "Doe", "123456789", user);

        
        String registerRequest = objectMapper.writeValueAsString(player);
        this.mvc.perform(post("/api/register").content(registerRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterError() throws Exception {

        User user = new User("test", "test");

        String registerRequest = objectMapper.writeValueAsString(user);
        this.mvc.perform(post("/api/register").content(registerRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
}
