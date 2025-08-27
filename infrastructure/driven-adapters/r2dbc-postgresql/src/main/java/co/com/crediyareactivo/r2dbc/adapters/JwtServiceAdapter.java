package co.com.crediyareactivo.r2dbc.adapters;

import co.com.crediyareactivo.model.user.gateways.ports.JWTServicePort;
import co.com.crediyareactivo.model.user.models.UserResponseDomain;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component
public class JwtServiceAdapter implements JWTServicePort {

    private static final String SECRET_KEY = "my-super-secret-key-for-jwt-signing-which-must-be-long";
    private static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    @Override
    public String generateToken(String email, Long rolId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("rol", rolId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    @Override
    public UserResponseDomain validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return UserResponseDomain.builder()
                .email(claims.getSubject())
                .rolName(claims.get("rol").toString())
                .build();
    }
}