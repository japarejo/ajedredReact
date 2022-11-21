package com.samples.ajedrez.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samples.ajedrez.user.AuthoritiesService;
import com.samples.ajedrez.user.User;
import com.samples.ajedrez.user.UserService;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;
	private UserService userService;
	private AuthoritiesService authoritiesService;

	@Autowired
	public PlayerService(PlayerRepository playerRepository, UserService userService,
			AuthoritiesService authoritiesService) {
		this.playerRepository = playerRepository;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
	}


    @Transactional
	public void savePlayer(Player player) throws DataAccessException {
		playerRepository.save(player);
		userService.saveUser(player.getUser());
		authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");
	}


	@Transactional(readOnly = true)
	public Player findPlayerByUsername(User username) throws DataAccessException {
		return playerRepository.findByUsername(username);
	}
}
