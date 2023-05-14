package com.samples.ajedrez.player;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samples.ajedrez.user.Authorities;
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


	public Player jugadorSesion(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return findPlayerByUsername(this.userService.findUser(username).orElse(null));
	}



	@Transactional
	public void updatePlayer(Player player) throws DataAccessException{

		

		User user = jugadorSesion().getUser();




		if(!user.getUsername().equals(player.getUser().getUsername())){
			List<Authorities> auths = user.getAuthorities();

			this.authoritiesService.deleteAuthorities(auths);

			this.userService.deleteUser(user);

			if(!user.getPassword().equals(player.getUser().getPassword())){
				this.userService.saveUser(player.getUser());
			}else{
				this.userService.updateUser(player.getUser());
			}

			authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");


			
		}else if(!user.getPassword().equals(player.getUser().getPassword())){
			this.userService.saveUser(player.getUser());
		}else{
			this.userService.updateUser(player.getUser());
		
		}

		playerRepository.save(player);
		


	}



	@Transactional
	public void updateTurnPlayer(Player player){
		playerRepository.save(player);
	}

	public List<Player> findAllPlayers(){
		return playerRepository.findAllPlayers();
	}

	public Player findPlayerById(int id){
		return playerRepository.findById(id);
	}
}
