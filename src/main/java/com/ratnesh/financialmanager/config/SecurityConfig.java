package com.ratnesh.financialmanager.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeRequests(req ->
                req.requestMatchers("/api/*").permitAll())
            .httpBasic(Customizer.withDefaults());
        // http
        //     .cors(cors -> cors.configurationSource(request -> {
        //         CorsConfiguration config = new CorsConfiguration();
        //         config.setAllowedOrigins(List.of("http://localhost:3000"));
        //         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        //         config.setAllowedHeaders(List.of("*"));
        //         return config;
        //     }))
        //     .csrf(AbstractHttpConfigurer::disable)
        //     .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    // Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}