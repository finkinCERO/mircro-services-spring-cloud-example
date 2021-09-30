package htw.kbe.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        try {
            Claims c = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return c;
        } catch (Exception ex) {
            System.out.println("#### #### ### getAllClaimsFromToken error");
        }
        return null;
    }

    private boolean isTokenExpired(String token) {
        Claims c = this.getAllClaimsFromToken(token);
        return c.getExpiration().before(new Date());
    }

    public boolean isInvalid(String token) {

        boolean b = this.isTokenExpired(token);
        return b;
    }

}
