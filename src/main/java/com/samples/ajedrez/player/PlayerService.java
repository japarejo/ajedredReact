package com.samples.ajedrez.player;

import com.samples.ajedrez.user.Authorities;
import com.samples.ajedrez.user.AuthoritiesService;
import com.samples.ajedrez.user.User;
import com.samples.ajedrez.user.UserService;
import com.samples.request.RegisterRequest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final UserService userService;
    private final AuthoritiesService authoritiesService;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, UserService userService,
            AuthoritiesService authoritiesService) {
        this.playerRepository = playerRepository;
        this.userService = userService;
        this.authoritiesService = authoritiesService;
    }

    @Transactional
    public void savePlayer(Player player) throws DataAccessException {
        
    	authoritiesService.saveAuthorities(player.getUser().getUsername(), "Player");
    	playerRepository.save(player);
    }

    @Transactional(readOnly = true)
    public Player findPlayerByUsername(String username) throws DataAccessException {
        return playerRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Player jugadorSesion() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return findPlayerByUsername(username);
    }

    @Transactional
    public void updatePlayer(Player player) throws DataAccessException {

        User user = jugadorSesion().getUser();

        if (!user.getUsername().equals(player.getUser().getUsername())) {
            List<Authorities> auths = user.getAuthorities();

            this.authoritiesService.deleteAuthorities(auths);

            this.userService.deleteUser(user);

            if (!user.getPassword().equals(player.getUser().getPassword())) {
                this.userService.saveUser(player.getUser());
            } else {
                this.userService.updateUser(player.getUser());
            }

            authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");

        } else if (!user.getPassword().equals(player.getUser().getPassword())) {
            this.userService.saveUser(player.getUser());
        } else {
            this.userService.updateUser(player.getUser());

        }

        playerRepository.save(player);

    }

    @Transactional
    public void updateTurnPlayer(Player player) {
        playerRepository.save(player);
    }
    
    @Transactional(readOnly = true)
    public List<Player> findAllPlayers() {
        return playerRepository.findAllPlayers();
    }

    @Transactional(readOnly = true)
    public Player findPlayerById(int id) {
        return playerRepository.findById(id);
    }
    
    public Player mapRegisterRequestToPlayer(RegisterRequest register, User user) {
    	String firstName = register.getFirstName();
    	String lastName = register.getLastName();
    	String telephone = register.getTelephone();
    	
    	Player player = new Player(firstName, lastName, telephone, user);
    	
    	return player;
    }
}
