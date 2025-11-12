package com.celesoft.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final com.celesoft.utils.security.JwtAuthenticationWebFilter jwtFilter;

    @Bean
    public WebFilter jwtSecurityFilter() {
        return jwtFilter;
    }
}
