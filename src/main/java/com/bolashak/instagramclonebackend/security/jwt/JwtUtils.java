package com.bolashak.instagramclonebackend.security.jwt;

import com.bolashak.instagramclonebackend.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${dev.jwtSecret}")
    private String jwtSecret;

    @Value("${dev.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try{
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        }
        catch(MalformedJwtException e){
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        catch(ExpiredJwtException e){
            logger.error("Expired JWT token: {}", e.getMessage());
        }
        catch(UnsupportedJwtException e){
            logger.error("Unsupported JWT token: {}", e.getMessage());
        }
        catch(IllegalArgumentException e){
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
