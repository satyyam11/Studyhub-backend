package com.studyhub.config;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    private final FirebaseAuth firebaseAuth;

    public WebSecurityConfig(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/files/**").authenticated() // Protect /api/files endpoints
                                .anyRequest().permitAll() // Allow other requests without authentication
                )
                .addFilterBefore(new AuthFilter(firebaseAuth), UsernamePasswordAuthenticationFilter.class) // Add custom filter
                .csrf(csrf -> csrf.disable()); // Disable CSRF

        return http.build();
    }
}
