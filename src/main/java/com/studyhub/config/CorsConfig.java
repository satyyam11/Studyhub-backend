package com.studyhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow all origins - you should restrict this in production
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000"); // React default port
        config.addAllowedOrigin("https://464e-14-139-226-226.ngrok-free.app"); // ngrok URL

        // Allow all headers
        config.addAllowedHeader("*");

        // Allow all methods
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
