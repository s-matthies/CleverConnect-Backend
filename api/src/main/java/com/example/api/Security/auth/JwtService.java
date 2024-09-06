package com.example.api.Security.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service für die Erstellung und Validierung von JWT-Token.
 */
@Service
public class JwtService {

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    /**
     * Extrahiert den Benutzernamen aus einem JWT-Token.
     * @param token Das JWT-Token
     * @return Der Benutzername
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrahiert ein Claim aus einem JWT-Token.
     * @param token Das JWT-Token
     * @param claimsResolver Funktion, die den Claim extrahiert
     * @param <T> Typ des Claims
     * @return Der extrahierte Claim
     */
    public<T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generiert ein JWT-Token für einen User.
     * @param userDetails Die UserDetails des Users
     * @return Das generierte JWT-Token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generiert ein JWT-Token für einen User mit zusätzlichen Claims.
     * @param extraclaims Zusätzliche Claims
     * @param userDetails Die UserDetails des Users
     * @return Das generierte JWT-Token
     */
    public String generateToken(
            Map<String, Object> extraclaims,
            UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraclaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 min
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Überprüft, ob ein JWT-Token abgelaufen ist.
     * @param token Das JWT-Token
     * @return true, wenn das Token abgelaufen ist, sonst false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrahiert das Ablaufdatum aus einem JWT-Token.
     * @param token Das JWT-Token
     * @return Das Ablaufdatum
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrahiert alle Claims aus einem JWT-Token.
     * @param token Das JWT-Token
     * @return Alle Claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gibt den Sign-In Key für die Erstellung und Validierung von JWT-Token zurück.
     * @return Der Sign-In Key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Überprüft, ob ein JWT-Token für einen User gültig ist.
     * @param jwt Das JWT-Token
     * @param userDetails Die UserDetails des Users
     * @return true, wenn das Token gültig ist, sonst false
     */
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        return extractUsername(jwt)
                .equals(userDetails.getUsername()) && !isTokenExpired(jwt);

    }

}


