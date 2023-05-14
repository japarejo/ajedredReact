package com.samples.ajedrez.player;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import com.samples.ajedrez.user.User;

@SpringBootTest
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @Test
    public void testFindPlayerByUsername() {

        Player player = new Player();
		player.setFirstName("Julio");
		player.setLastName("Rodriguez");
		player.setTelephone("687978879");
		User user = new User();
		user.setUsername("prueba");
		user.setPassword("prueba");
		user.setEnabled(true);
		player.setUser(user);
		this.playerService.savePlayer(player);
		Player p = this.playerService.findPlayerByUsername(user);
		assertThat(p.getFirstName().equals("Julian"));
        
    }


    @Test
	public void testInsertPlayer() {
		List<Player> players = this.playerService.findAllPlayers();
		int found = players.size();

		Player player = new Player();
		player.setFirstName("Julio");
		player.setLastName("Rodriguez");
		player.setTelephone("687987978");
		User user = new User();
		user.setUsername("Julian");
		user.setPassword("supersecretpassword");
		user.setEnabled(true);
		player.setUser(user);

        this.playerService.savePlayer(player);

		players = this.playerService.findAllPlayers();
		assertThat(players.size()).isEqualTo(found + 1);
	}


    @Test
	public void testUpdatePlayer() {
		Player player = this.playerService.findPlayerById(1);
		String oldLastName = player.getLastName();
		String newLastName = oldLastName + "X";

		player.setLastName(newLastName);
		this.playerService.savePlayer(player);

		player = this.playerService.findPlayerById(1);
		assertThat(player.getLastName()).isEqualTo(newLastName);
	}
    
}
