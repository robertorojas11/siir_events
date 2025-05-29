package com.siir.itq.events.config.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.siir.itq.events.config.exceptions.CustomExceptions.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            
        try{
            String token = request.getHeader("Authorization");
            if(token==null){
                filterChain.doFilter(request, response);
                return;
            }

            token = cutToken(token);
            jwtHelper.validateToken(token);

            String username = jwtHelper.getTokenSubject(token);
            String tipoUsuario = jwtHelper.getTipoUsuario(token);
            List<GrantedAuthority> authorities = 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + tipoUsuario));

            UsernamePasswordAuthenticationToken authenticationToken 
                = new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        }catch(NotAuthorizedException e){
            throw new NotAuthorizedException(e.getMessage());
        }   
    }

    private String cutToken(String token){
        if(token.startsWith("Bearer")){
            return token.substring(7);
        }
        return token;
    }
}
