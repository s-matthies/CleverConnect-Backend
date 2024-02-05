package com.example.api.Security.config;


import com.example.api.Security.auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Konfiguration für die Authentifizierung.
 */
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


    /**
     * Erstellt ein SecurityFilterChain-Objekt.
     * @param http HttpSecurity-Objekt für die Konfiguration der Zugriffsrechte
     * @return SecurityFilterChain-Objekt für die Konfiguration der Filterkette
     */
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                // CSRF-Schutz wird deaktiviert
                .csrf((csrf) -> csrf.disable())
                // Konfiguration der Zugriffsrechte
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                //.requestMatchers("/", "/user/register, "/external/register", "/user/login").permitAll()
                                .requestMatchers("/user/**",
                                        "/external/**",
                                        "/admin/**",
                                        "/swagger-ui/**" ,
                                        "/api-docs/**",
                                        "/swagger-ui.html"
                                ).permitAll()
                                //.requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                                //.requestMatchers("/external/**").hasAnyRole("EXTERN","ADMIN")
                                //.requestMatchers("/admin/**").hasRole("ADMIN")
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

