package com.sparta.deliverypathservice.global.config;

import com.sparta.deliverypathservice.global.domain.user.User;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

//    @Bean
//    public AuditorAware<Long> auditorProvider() {
//        return new AuditorAwareImpl();
//    }
//
//    public static class AuditorAwareImpl implements AuditorAware<Long> {
//
//        @Override
//        public Optional<Long> getCurrentAuditor() {
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication == null || !authentication.isAuthenticated()) {
//                return Optional.empty();
//            }
//
//            return Optional.of(((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId());
//        }
//    }
}
