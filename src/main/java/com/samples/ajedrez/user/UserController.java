package com.samples.ajedrez.user;



//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samples.ajedrez.service.JwtUtilService;
import com.samples.ajedrez.service.TokenInfo;

@RequestMapping("")
@RestController
//@CrossOrigin(origins = "*")
public class UserController {
    

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    //private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/login")
    public ResponseEntity<?> authenticate (@RequestBody User user) {

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            

            final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(user.getUsername());

            final String jwt = jwtUtilService.generateToken(userDetails);

            TokenInfo tokenInfo = new TokenInfo(jwt);

            return ResponseEntity.ok(tokenInfo);

        } catch(AuthenticationException e){
            return ResponseEntity.notFound().build();
        }


        

        
    }
}
