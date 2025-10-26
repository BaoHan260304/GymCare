package base.api.config;

import base.api.model.Account;
import base.api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import base.api.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;

    public JwtUtil() {
        String secret = "MyVerySecretKeyThatIsLongEnough123456789";
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(CustomUserDetails userDetails) {
        // Step 1: Get the Account and User objects from CustomUserDetails
        Account account = userDetails.getAccount();
        User user = account.getUser();

        // Handle cases where the user object might be null (though it shouldn't be in a valid login)
        String userName = (user != null) ? user.getUserName() : account.getAccountName();
        Long userId = (user != null) ? user.getId() : null;

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // This is email
                .claim("email", userDetails.getUsername())
                .claim("user_name", userName) // Use the actual user's name
                .claim("role", account.getRole()) // Get role from the account
                .claim("id", userId) // Get user's ID
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 100))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return parseAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = parseAllClaims(token);
        return claims.get("id", Long.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())
                && !parseAllClaims(token).getExpiration().before(new Date()));
    }

}
