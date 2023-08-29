package com.samples.ajedrez.service;

import com.samples.ajedrez.game.GameService;
import com.samples.ajedrez.plan.Plan;
import com.samples.ajedrez.user.Authorities;
import com.samples.ajedrez.user.User;
import com.samples.ajedrez.user.UserService;

import io.github.isagroup.PricingContext;
import io.github.isagroup.PricingEvaluatorUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtilServiceNew extends PricingContext {

    @Value("${jwt.secret}")
    private String JWT_SECRET_KEY;

    public static final int JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * 8; // 8 Horas

    private final UserService userService;

    private final GameService gameService;

    @Autowired
    private PricingEvaluatorUtil pricingEvaluatorUtil;

    @Autowired
    public JwtUtilServiceNew(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public String getJwtSecret() {
        return JWT_SECRET_KEY;
    }

    @Override
    public String getConfigFilePath() {
        return "pricings/config.yml";
    }

    @Override
    public Map<String, Object> getUserContext() {

        User user = userService.findCurrentUser();
        return generateUserContext(user.getUsername());
    }

    @Override
    public String getUserPlan() {
        User user = userService.findCurrentUser();
        Plan plan = user.getPlan();
        return plan.getType().toString();
    }

    @Override
    public Object getUserAuthorities() {
        User user = userService.findCurrentUser();
        return generateUserAuthorities(user);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // New version of generate token with no parameters
    public String generateToken() {
        return pricingEvaluatorUtil.generateUserToken();
    }

    private Map<String, Object> generateUserAuthorities(User user) {
        Map<String, Object> userAuthorities = new HashMap<>();
        List<String> authorities;

        String username = user.getUsername();
        Plan plan = user.getPlan();
        authorities = user.getAuthorities().stream().map(Authorities::getAuthority).collect(Collectors.toList());
        userAuthorities.put("username", username);
        userAuthorities.put("role", authorities);
        userAuthorities.put("plan", plan.getType());

        return userAuthorities;
    }

    private Map<String, Object> generateUserContext(String username) {
        Map<String, Object> planContext = new HashMap<>();
        int gamesPlayed = this.gameService.getGamesPlayedByPlayerUsername(username);
        planContext.put("username", username);
        planContext.put("allow_game_spectators", false);
        planContext.put("games_played", gamesPlayed);

        return planContext;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
