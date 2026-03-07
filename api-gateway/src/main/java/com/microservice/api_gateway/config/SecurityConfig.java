package com.microservice.api_gateway.config;

import com.microservice.api_gateway.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtFilter();
    }
}
