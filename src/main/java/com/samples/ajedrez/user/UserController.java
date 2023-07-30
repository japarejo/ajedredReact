package com.samples.ajedrez.user;

import com.samples.ajedrez.plan.Plan;
import com.samples.ajedrez.plan.PlanService;
import com.samples.ajedrez.player.Player;
import com.samples.ajedrez.player.PlayerService;
import com.samples.ajedrez.service.JwtUtilService;
import com.samples.request.LoginRequest;
import com.samples.request.RegisterRequest;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService usuarioDetailsService;

    private final JwtUtilService jwtUtilService;

    private final PlayerService playerService;

    private final UserService userService;
    
    private final PlanService planService;
    
    @Autowired
    public UserController(AuthenticationManager authManager,
    			UserDetailsService userDetailsService,
    			JwtUtilService jwtService,
    			PlayerService playerService,
    			UserService userService,
    			PlanService planService) {
    	
    	this.authenticationManager = authManager;
    	this.usuarioDetailsService = userDetailsService;
    	this.jwtUtilService = jwtService;
    	this.playerService = playerService;
    	this.userService = userService;
    	this.planService = planService;
    	
    }

    @Value("${jwt.secret}")
    private String JWT_SECRET_KEY;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    @GetMapping("/auth/validate_token/{token}")
    public ResponseEntity<String> tokenValido(@PathVariable String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(token);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON).
                    body(e.getMessage());
        }

    }
    
    @GetMapping("/check")
    public ResponseEntity<String> checkHealth() {
        String message = "I am running ✅";
        return ResponseEntity.ok()
                .header("Content-Type", "application/json;charset=utf8")
                .body(message);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@Valid @RequestBody LoginRequest login) {

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

            final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(login.getUsername());

            final String jwt = jwtUtilService.generateToken(userDetails);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jwt);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest register) {

      
        if (userService.checkUsernameExists(register.getUsername())) {
            return ResponseEntity.badRequest().build();

        }
        
        if (!planService.checkPlanExists(register.getPlan())) {
            return ResponseEntity.badRequest().build();

        }
        
        String planName = register.getPlan();
        
        Plan plan = this.planService.getPlanByPlanType(planName);
        User user = this.userService.mapRegisterToUserEntity(register, plan);
        
        this.userService.saveUser(user);
        
        Player player = playerService.mapRegisterRequestToPlayer(register, user);
        playerService.savePlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
