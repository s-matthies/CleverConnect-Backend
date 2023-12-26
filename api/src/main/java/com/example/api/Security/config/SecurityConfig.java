package com.example.api.Security.config;

import com.example.api.Service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Konfiguration der Zugriffsrechte
    // Jeder kann sich registrieren und einloggen
    // Studenten können auf die Studenten-API zugreifen
    // Externe können auf die Externe-API zugreifen
    // Admins können auf die Admin-API zugreifen
    // Alle anderen Anfragen müssen authentifiziert sein
    // CSRF-Schutz wird deaktiviert
    // Login- und Logout-URLs werden festgelegt
    // Login-Page wird festgelegt
    // CSRF-Schutz wird deaktiviert
    // Login- und Logout-URLs werden festgelegt
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                /*
                .requestMatchers("/api/user/registration", "/api/user/login", "/h2-console/**").permitAll()
                .requestMatchers("/api/user/**").hasRole("STUDENT")
                .requestMatchers("/api/external/**").hasRole("EXTERN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()

                 */

                .and()
                .formLogin(form -> form
                        .loginPage("/api/user/login")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/api/user/logout")
                        .permitAll()
                )

                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /*
    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http.authorizeHttpRequests(c ->
            c.requestMatchers("/", "/home", "/register", "/css/**.css", "/js/**")
            .permitAll().anyRequest().authenticated())
                    .formLogin(c -> c.loginPage("/login").permitAll())
                    .logout(LogoutConfigurer::permitAll)
                    .build();
        }
     */



    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }




}
