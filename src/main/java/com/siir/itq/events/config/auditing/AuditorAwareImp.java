package com.siir.itq.events.config.auditing;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImp implements AuditorAware<String>{

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof String principal){
            return Optional.of(principal);
        }

        return Optional.of("System");
    }

}