package com.radioheaven.radioheaven_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // Security configuration can be added here
    // For example, you can configure HTTP security, authentication providers, etc.
    // This is a placeholder for future security configurations.

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/api/songs/**").permitAll() // Allow unauthenticated access to auth endpoints
                        .anyRequest().authenticated() // Require authentication for any other request
                        ).
                        csrf(AbstractHttpConfigurer::disable)
                        .cors(CorsConfigurer::disable)// Disable CSRF protection for simplicity, adjust as needed
                        .build();
    }

}
