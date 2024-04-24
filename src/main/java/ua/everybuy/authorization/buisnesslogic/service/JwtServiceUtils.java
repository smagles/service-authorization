package ua.everybuy.authorization.buisnesslogic.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ua.everybuy.authorization.database.entity.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component  //TODO Service?
public class JwtServiceUtils {
    @Value("${jwt.secret}")
    private String secretKeyString;
    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;
    private SecretKey secretKey;

//    JwtServiceUtils() {
//        secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
//    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
        List<String> rolesList = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("user_id", user.getId());
        //claims.put("email", user.getEmail());
        claims.put("phone", user.getPhoneNumber());
        claims.put("roles", rolesList);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String getEmail(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    public String getPhone(String token) {
        return getAllClaimsFromToken(token).get("phone", String.class);
    }

    public Long getUserId(String token) {
        return getAllClaimsFromToken(token).get("user_id", Long.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey()).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
