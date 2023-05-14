package com.samples.ajedrez.user;




import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samples.ajedrez.player.Player;
import com.samples.ajedrez.player.PlayerService;
import com.samples.ajedrez.service.JwtUtilService;
import com.samples.ajedrez.service.TokenInfo;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;


@RequestMapping("/api")
@RestController
//@CrossOrigin(origins = "*")
public class UserController {
    

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private PlayerService playerService;

    @Value("${jwt.secret}")
    private String JWT_SECRET_KEY;

    @InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

    @GetMapping("/auth/validate_token/{token}")
    public String tokenValido(@PathVariable String token) {

        try {
            //Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes())).parseClaimsJws(accessToken);

            Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token);

            return "200";
        } catch (JwtException e) {
            return "401";
        }

    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate (@RequestBody User user) {

        try{

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            

            final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(user.getUsername());
            

            final String jwt = jwtUtilService.generateToken(userDetails);

            TokenInfo tokenInfo = new TokenInfo(jwt);

            return ResponseEntity.ok(tokenInfo.getJwtToken());

        } catch(AuthenticationException e){
            return ResponseEntity.badRequest().build();
        }
    }



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Player player){


        try{
            playerService.savePlayer(player);

            return ResponseEntity.ok().build();
            
        }catch(DataAccessException e){
            return ResponseEntity.badRequest().build();
        }
    }


}
