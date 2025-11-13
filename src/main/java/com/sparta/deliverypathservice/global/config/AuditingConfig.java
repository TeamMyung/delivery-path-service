package com.sparta.deliverypathservice.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

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
