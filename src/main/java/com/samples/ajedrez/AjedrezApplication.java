package com.samples.ajedrez;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.samples.ajedrez.user.User;
import com.samples.ajedrez.user.UserService;

@SpringBootApplication
public class AjedrezApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(AjedrezApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (userService.findAllUsers().isEmpty()) {
			User user1 = new User();
			user1.setUsername("dani");
			user1.setPassword(new BCryptPasswordEncoder().encode("react"));
			userService.saveUser(user1);
		
		}
	}
	

}
