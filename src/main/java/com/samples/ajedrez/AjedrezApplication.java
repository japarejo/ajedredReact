package com.samples.ajedrez;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.samples.ajedrez.player.Player;
import com.samples.ajedrez.player.PlayerService;
import com.samples.ajedrez.user.User;
import com.samples.ajedrez.user.UserService;

@SpringBootApplication
public class AjedrezApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private PlayerService playerService;

	public static void main(String[] args) {
		SpringApplication.run(AjedrezApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (userService.findAllUsers().isEmpty()) {
			User user1 = new User();
			user1.setUsername("dani");
			user1.setPassword("react");

			Player player1 = new Player();

			player1.setFirstName("Daniel");
			player1.setLastName("Rodriguez");
			player1.setTelephone("654765767");
			player1.setUser(user1);

			playerService.savePlayer(player1);

			User user2 = new User();
			user2.setUsername("adri");
			user2.setPassword("react");

			Player player2 = new Player();

			player2.setFirstName("Adrian");
			player2.setLastName("Contreras");
			player2.setTelephone("678675876");
			player2.setUser(user2);

			playerService.savePlayer(player2);

		}
	}

}
