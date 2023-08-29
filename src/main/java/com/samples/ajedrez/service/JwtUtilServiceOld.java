package com.samples.ajedrez.service;

import com.samples.ajedrez.game.GameService;
import com.samples.ajedrez.plan.Plan;
import com.samples.ajedrez.user.Authorities;
import com.samples.ajedrez.user.User;
import com.samples.ajedrez.user.UserService;
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
import org.springframework.stereotype.Service;

@Service
public class JwtUtilServiceOld {

    @Value("${jwt.secret}")
    private String JWT_SECRET_KEY;

    public static final int JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * 8; // 8 Horas

    private final UserService userService;

    private final GameService gameService;

    @Autowired
    public JwtUtilServiceOld(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
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

    public String generateToken(UserDetails userDetails) {

        User user = this.userService.findUser(userDetails.getUsername())
                .orElse(null);

        String username = user.getUsername();
        Plan plan = user.getPlan();

        Map<String, Object> userAuthorities = generateUserAuthorities(user);
        Map<String, Object> userContext = generateUserContext(username);
        Map<String, String> evaluationContext = generateEvaluationContext();
        Map<String, Object> planContext = generatePlanContext(plan);

        // PricingEvaluatorUtil util = new PricingEvaluatorUtil(planContext,
        // evaluationContext, userContext,
        // userAuthorities, JWT_SECRET_KEY, JWT_TOKEN_VALIDITY);
        // String token = util.generateUserToken();

        return "Warning!!! Broken changes in api, bumped to 2.0.0. Removed comments above will not compile (Using version 1.1.0).";
    }

    private Map<String, Object> generateUserAuthorities(User user) {
        Map<String, Object> userAuthorities = new HashMap<>();
        List<String> authorities;

        String username = user.getUsername();
        Plan plan = user.getPlan();
        authorities = user.getAuthorities().stream().map(Authorities::getAuthority).collect(Collectors.toList());
        userAuthorities.put("username", username);
        userAuthorities.put("role", authorities);
        userAuthorities.put("plan_type", plan.getType());

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

    private Map<String, String> generateEvaluationContext() {
        Map<String, String> evaluationContext = new HashMap<>();

        evaluationContext.put("max_games", "userContext['games_played'] <= planContext['max_games']");
        evaluationContext.put("allow_game_spectators", "planContext['allow_game_spectators']");

        return evaluationContext;
    }

    private Map<String, Object> generatePlanContext(Plan plan) {
        Map<String, Object> planContext = new HashMap<>();

        planContext.put("allow_game_spectators", plan.isAllowGameSpectators());
        planContext.put("max_games", plan.getMaxGames());

        return planContext;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}