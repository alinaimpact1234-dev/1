package com.impact.lessons.config;

import com.impact.lessons.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1. Dezactivăm CSRF și setăm sesiunea pe STATELESS (standard pentru JWT)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 2. Un singur bloc de autorizare cu TOATE rutele ordonate corect
                .authorizeHttpRequests(auth -> auth
                        // Rutele publice pentru Swagger / OpenAPI
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Rutele publice pentru autentificare și crearea utilizatorilor
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/create").permitAll()

                        // Rutele restricționate în funcție de roluri
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/content/**").hasAnyRole("ADMIN", "AUTHOR", "EDITOR")

                        // Regula universală - OBLIGATORIU ULTIMA LINIE
                        .anyRequest().authenticated()
                )

                // 3. Adăugarea filtrului JWT înainte de cel standard
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
