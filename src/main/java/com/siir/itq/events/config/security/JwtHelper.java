package com.siir.itq.events.config.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.siir.itq.events.config.exceptions.CustomExceptions.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;

@Component
public class JwtHelper {

    @Value("${jwt.secret}")
    private String jwtKey;

    private SecretKey secret;

    @PostConstruct
    public void init(){
        secret = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    }

    public String getTokenSubject(String token){
        return getTokenBody(token).getSubject();
    }

    public Claims getTokenBody(String token){
        return Jwts.parserBuilder()
            .setSigningKey(secret).build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String getTipoUsuario(String token){
        return getTokenBody(token).get("rol").toString();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        }catch(SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e){
            throw new NotAuthorizedException ("Token invalido");
        }catch(ExpiredJwtException e){
            throw new NotAuthorizedException ("Token expirado");
        }

    }
}
