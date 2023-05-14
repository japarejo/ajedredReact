package com.samples.ajedrez.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    protected UserService userService;
    
    @Test
    public void testFindUserById() {

        User foundUser = userService.findUser("dani").get();
        assertEquals("dani", foundUser.getUsername());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        assertTrue(encoder.matches("react", "$2a$10$gQDWtxQZtOVRbvGoAtdWPOdcvh1htqoRVE4O/unVDF/0v383KwuqC"));
        
    }
    
    

    @Test
    public void testsaveUser(){
        
        User newUsuario = new User();
		newUsuario.setUsername("usuarioPrueba");
		newUsuario.setPassword("12345prueba");
		Authorities aut = new Authorities();
		aut.setAuthority("player");
		aut.setUser(newUsuario);
		List<Authorities> list_aut = new ArrayList<>();
		list_aut.add(aut);
		newUsuario.setAuthorities(list_aut);
		userService.saveUser(newUsuario);

        assertThat(userService.findUser("usuarioPrueba").get().getUsername()).isEqualTo(newUsuario.getUsername());

    }

    
    
}
