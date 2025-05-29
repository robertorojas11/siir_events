package com.siir.itq.events.config.auditing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.siir.itq.events.enums.TipoUsuario;


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorService")
public class JpaConfig{

    @Bean
    public AuditorAware<String> auditorService(){
        return new AuditorAwareImp();
    }

    public static void setAuthorityForRegistration(String noControl, TipoUsuario tipoUsuario){
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

        if(existingAuth!=null && existingAuth.getPrincipal().toString().equals("anonymousUser")){
            
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + tipoUsuario));

            Authentication auth = new UsernamePasswordAuthenticationToken(noControl, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }
}