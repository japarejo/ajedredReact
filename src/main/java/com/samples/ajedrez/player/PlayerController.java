package com.samples.ajedrez.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samples.ajedrez.service.JwtUtilService;
import com.samples.ajedrez.service.TokenInfo;

@RequestMapping("/api/player")
@RestController
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @Autowired
    UserDetailsService usuarioDetailsService;

    @Autowired
    JwtUtilService jwtUtilService;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/data")
    public Player getPlayer() {

        Player player = this.playerService.jugadorSesion();

        return player;
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody Player player) {

        Player jugadorActual = this.playerService.jugadorSesion();

        player.setId(jugadorActual.getId());

        try {

            if (!player.getUser().getUsername().equals(jugadorActual.getUser().getUsername())) {

                Player playerWithUser = this.playerService.findPlayerByUsername(player.getUser());

                if (playerWithUser != null) {
                    throw new Error();
                }

                this.playerService.updatePlayer(player);

                final UserDetails userDetails = usuarioDetailsService
                        .loadUserByUsername(player.getUser().getUsername());

                final String jwt = jwtUtilService.generateToken(userDetails);

                TokenInfo tokenInfo = new TokenInfo(jwt);

                return ResponseEntity.ok(tokenInfo);

            } else {
                this.playerService.updatePlayer(player);
                return ResponseEntity.ok().build();
            }

        } catch (DataAccessException e) {
            return ResponseEntity.badRequest().build();

        } catch (Error e) {
            return ResponseEntity.internalServerError().build();
        }

    }

}
