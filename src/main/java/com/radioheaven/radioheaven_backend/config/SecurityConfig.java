package com.radioheaven.radioheaven_backend.config;

import com.google.firebase.auth.FirebaseAuth;
import com.radioheaven.radioheaven_backend.security.FirebaseAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final FirebaseAuth firebaseAuth;

    public SecurityConfig(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    // Security configuration: require authentication for all endpoints except /api/public/**
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/api/public/**").permitAll() // Allow unauthenticated access to public endpoints
                                .anyRequest().authenticated() // Require authentication for any other request
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(CorsConfigurer::disable) // Disable CORS here; controller has @CrossOrigin where needed
                // Register FirebaseAuthenticationFilter to validate Bearer ID tokens
                .addFilterBefore(new FirebaseAuthenticationFilter(firebaseAuth), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
