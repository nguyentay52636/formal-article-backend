package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html").permitAll()
                .requestMatchers("/v3/api-docs/**", "/api-docs/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable());
        
        return http.build();
    }
}
