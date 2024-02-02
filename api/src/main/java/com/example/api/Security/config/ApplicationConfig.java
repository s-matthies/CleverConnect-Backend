package com.example.api.Security.config;

import com.example.api.Repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Konfiguration für die Authentifizierung.
 */
@Configuration
public class ApplicationConfig {

    private final UserRepository userRepository;

    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Erstellt ein UserDetailsService-Objekt.
     * @return UserDetailsService-Objekt für die Authentifizierung
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmailIgnoreCase(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Erstellt ein AuthenticationProvider-Objekt.
     * @return AuthenticationProvider-Objekt für die Authentifizierung
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Erstellt ein AuthenticationManager-Objekt.
     * @param config AuthenticationConfiguration-Objekt für die Konfiguration der Authentifizierung
     * @return AuthenticationManager-Objekt für die Authentifizierung
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Erstellt ein BCryptPasswordEncoder-Objekt.
     * @return BCryptPasswordEncoder-Objekt für das Hashen von Passwörtern
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

