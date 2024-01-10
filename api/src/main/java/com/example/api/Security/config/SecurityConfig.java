package com.example.api.Security.config;

import com.example.api.Security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                // CSRF-Schutz wird deaktiviert
                .csrf((csrf) -> csrf.disable())
                // Konfiguration der Zugriffsrechte
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                //.requestMatchers("/", "/user/register",
                                //       "/external/register",
                                //      "/user/login").permitAll()
                                .requestMatchers("/user/**", "/external/**").permitAll()
                                //.requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                                //.requestMatchers("/external/**").hasAnyRole("EXTERN","ADMIN")
                                .anyRequest().authenticated()

                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider);


        return http.build();
    }


}

