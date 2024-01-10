package com.example.api.Security.auth;

import com.example.api.Repository.UserRepository;
import com.example.api.Security.jwt.AuthenticationRequest;
import com.example.api.Security.jwt.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private  final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final com.example.api.Security.jwt.JwtService JwtService;


    public AuthenticationService(UserRepository repository,
                                 AuthenticationManager authenticationManager, com.example.api.Security.jwt.JwtService jwtService) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        JwtService = jwtService;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow();
        var jwtToken = JwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

}


