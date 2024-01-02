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


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF-Schutz wird deaktiviert
                .csrf((csrf) -> csrf.disable())
                // Konfiguration der Zugriffsrechte


                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/", "/user/register",
                                        "/external/register",
                                        "/user/login").permitAll()
                                .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/external/**").hasAnyRole("EXTERN","ADMIN")
                                .anyRequest().authenticated()
                )

                // Login- und Logout-URLs werden festgelegt
                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login")
                            .usernameParameter("email")
                            .passwordParameter("password")
                        .loginProcessingUrl("/user/login")
                        //.defaultSuccessUrl("/dashboard")
                        .permitAll()
                )
                .logout((logout) ->
                        logout//.deleteCookies("remove")
                                .invalidateHttpSession(false)
                                .logoutUrl("/user/logout")
                                .logoutSuccessUrl("/")
                );



        return http.build();
    }
    

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

}
