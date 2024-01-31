package com.example.api.Security.auth;

import com.example.api.Repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


/**
 * Service für die Authentifizierung von Benutzer*innen.
 */
@Service
public class AuthenticationService {
    private  final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtService JwtService;

    /**
     * Konstruktor der Klasse AuthenticationService.
     * @param repository Das UserRepository-Objekt für den Zugriff auf Benutzer*innendaten
     * @param authenticationManager AuthenticationManager für die Authentifizierung von Benutzer*innen
     * @param jwtService JwtService für die Erstellung von JWT-Token
     */
    public AuthenticationService(UserRepository repository,
                                 AuthenticationManager authenticationManager,
                                 JwtService jwtService) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.JwtService = jwtService;
    }

    /**
     * Authentifiziert eine Benutzer*in und gibt ein JWT-Token zurück.
     * @param request Die Anfrage mit den Daten der zu authentifizierenden Benutzer*in (mit E-Mail-Adresse und Passwort)
     * @return AuthenticationResponse mit dem generierten JWT-Token
     * @throws Exception wenn die Authentifizierung fehlschlägt oder die Benutzer*in nicht gefunden wird
     */
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


