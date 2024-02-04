package com.example.api.Service;

import com.example.api.DTO.SignInResponse;
import com.example.api.Entitys.Role;
import com.example.api.Entitys.User;
import com.example.api.Repository.UserRepository;
import com.example.api.Request.UserRequest;
import com.example.api.Security.auth.AuthenticationRequest;
import com.example.api.Security.auth.AuthenticationResponse;
import com.example.api.Security.auth.AuthenticationService;
import com.example.api.Security.auth.JwtService;
import com.example.api.UserNotFound.UserNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * Service-Klasse für die Verwaltung von Benutzern.
 */
@Service
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;


    /**
     * Konstruktor für die UserService-Klasse.
     *
     * @param userRepository        Das UserRepository-Objekt, das für den Zugriff auf die Datenbank verwendet wird.
     * @param bCryptPasswordEncoder Das BCryptPasswordEncoder-Objekt, das für die Verschlüsselung von Passwörtern
     *                              verwendet wird.
     * @param emailService          Das EmailService-Objekt, das für den Versand von E-Mails verwendet wird.
     * @param jwtService            Das JwtService-Objekt, das für die Erstellung und Überprüfung von JWT-Tokens
     *                              verwendet wird.
     * @param authenticationService Das AuthenticationService-Objekt, das für die Authentifizierung von Benutzern
     *                              verwendet wird.
     */
    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService,
                       JwtService jwtService,
                       AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    /**
     * Lädt einen User anhand der E-Mail-Adresse.
     *
     * @param email Die E-Mail-Adresse des zu ladenden Users.
     * @return Der gefundene User.
     * @throws UsernameNotFoundException Falls der User nicht gefunden wird.
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        // Überprüfen, ob ein User mit der angegebenen E-Mail-Adresse vorhanden ist
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    /**
     * Registriert einen User und gibt eine entsprechende JSON-Antwort zurück.
     *
     * @param user Der zu registrierende User.
     * @return ResponseEntity mit einer Erfolgsmeldung oder Fehlermeldung und dem entsprechenden HTTP-Status.
     */
    public ResponseEntity<Object> userRegistration(User user) {
        try {
            // Überprüfen, ob die E-Mail-Adresse bereits vorhanden ist
            boolean userExists = userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent();

            if (userExists) {
                throw new IllegalStateException("E-Mail Adresse ist bereits vergeben!");
            }

            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setRegistrationDate(LocalDate.now()); // das Registrierungsdatum auf das aktuelle Datum setzen

            User savedUser = userRepository.save(user);

            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String email = user.getEmail();

            emailService.sendWelcomeEmailUser(email, firstName, lastName);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalStateException e) {
            String errorMessage = "{\"error\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    /**
     * Registriert einen User und gibt eine entsprechende JSON-Antwort zurück.
     *
     * @param userRequest Die Anfrage mit den Daten des zu registrierenden Users.
     * @return ResponseEntity mit einer Erfolgsmeldung oder Fehlermeldung und dem entsprechenden HTTP-Status.
     */
    public ResponseEntity<Object> register(UserRequest userRequest){
        return userRegistration(new User(
                userRequest.getFirstname(),
                userRequest.getLastname(),
                userRequest.getEmail(),
                userRequest.getPassword(),
                null,
                Role.STUDENT,
                false,
                true
        ));
    }

    /**
     * Lädt einen User anhand der angegebenen ID.
     *
     * @param id Die ID des zu ladenden Users.
     * @return Der gefundene User.
     * @throws UserNotFoundException Falls der User nicht gefunden wird.
     */
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }


    /**
     * Lädt alle User aus der Datenbank.
     *
     * @return Eine Liste aller User.
     */
    public List<User> allUser() {
        return userRepository.findAll();
    }


    /**
     * Aktualisiert die Daten eines Users anhand der angegebenen ID.
     *
     * @param id      Die ID des zu aktualisierenden Users.
     * @param newUser Die neuen Daten des Users.
     * @return ResponseEntity mit einer Erfolgsmeldung und dem HTTP-Status OK.
     * @throws UserNotFoundException Falls der User nicht gefunden wird.
     */
    public ResponseEntity<User> updateUser(Long id, User newUser) {

            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            //existierende USerin mit neuen Daten updaten
            existingUser.setFirstName(newUser.getFirstName());
            existingUser.setLastName(newUser.getLastName());
            existingUser.setEmail(newUser.getEmail());

            User savedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(savedUser);
    }


    /**
     * Löscht einen User anhand der angegebenen ID.
     *
     * @param id Die ID des zu löschenden Users.
     * @return ResponseEntity mit einer Erfolgsmeldung und dem HTTP-Status OK.
     * @throws UserNotFoundException Falls der User nicht gefunden wird.
     */
    public ResponseEntity<Object> deleteUser(Long id) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            userRepository.delete(existingUser);

            String message = "{\"User mit der ID \"" + id + "\" erfolgreich gelöscht!\"}";
            return ResponseEntity.ok(message);
        }
        catch (UserNotFoundException e) {
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }


    /**
     * Meldet einen User anhand der E-Mail-Adresse und des Passworts an und gibt eine entsprechende JSON-Antwort zurück.
     *
     * @param email       Die E-Mail-Adresse des Users.
     * @param password    Das Passwort des Users.
     * @return ResponseEntity mit einer Erfolgsmeldung oder Fehlermeldung und dem entsprechenden HTTP-Status.
     */
    public ResponseEntity<SignInResponse> signInUser(String email, String password) {
        try {
            // Authentifizierung des Benutzers durch die AuthenticationService
            AuthenticationResponse authenticationResponse = authenticationService.authenticate(
                    new AuthenticationRequest(email, password)
            );

            if (authenticationResponse == null) {
                throw new IllegalStateException("Login war nicht erfolgreich! " +
                        "Email oder Passwort nicht korrekt!");
            }

            User user = (User) loadUserByUsername(email);
            Role role = user.getRole();

            // Erstellen des SignInResponse-Objekts
            SignInResponse signInResponse = new SignInResponse();
            signInResponse.setToken(authenticationResponse.getToken());
            signInResponse.setRole(role);
            signInResponse.setId(user.getId());

            return ResponseEntity.ok(signInResponse);
        } catch (BadCredentialsException e) {
            SignInResponse errorResponse = new SignInResponse();
            errorResponse.setToken("Login war nicht erfolgreich! Email oder Passwort nicht korrekt!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        catch (IllegalStateException e) {
            SignInResponse errorResponse = new SignInResponse();
            errorResponse.setToken(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    /**
     * Methode für das Ausloggen eines Benutzers.
     *
     * @param request Die Anfrage mit den Daten des zu ausloggenden Users.
     * @param response Die Antwort mit den Daten des zu ausloggenden Users.
     * @return ResponseEntity mit einer Erfolgsmeldung oder Fehlermeldung und dem entsprechenden HTTP-Status.
     */
    public ResponseEntity<Object> signOut(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Überprüfen, ob der Benutzer ein gültiges JWT-Token im Header hat
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new IllegalStateException("Es ist kein Benutzer eingeloggt.");
            }

            // Token aus dem Authorization-Header extrahieren
            String token = authorizationHeader.substring(7);

            // Überprüfen, ob Token gültig ist
            if (!jwtService.isTokenValid(token, loadUserByUsername(jwtService.extractUsername(token))))
                throw new IllegalStateException("Das Token ist ungültig oder abgelaufen.");

            // die Session des Users ungültig machen
            request.getSession().invalidate();

            // das JWT-Token im Cookie löschen
            Cookie cookie = getCookie();
            response.addCookie(cookie);

            String message = "Logout erfolgreich";
            return ResponseEntity.ok().body("{\"message\": \"" + message + "\"}");
        } catch (IllegalStateException e) {
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }

    /**
     * Erstellt ein Cookie-Objekt, das das JWT-Token enthält.
     *
     * @return Das erstellte Cookie-Objekt.
     */
    private static Cookie getCookie() {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setMaxAge(0); // Ablaufdatum auf null (Vergangenheit) setzen
        cookie.setSecure(true); // Secure-Flag setzen / Cookie wird nur über sichere Verbindungen übertragen (HTTPS)
        cookie.setHttpOnly(true); // HttpOnly-Flag setzen / Cookie kann nicht von clientseitigen Skripten (z.B. JavaScript) ausgelesen werden
        cookie.setPath("/"); // Der Pfad wird auf "/" gesetzt
        return cookie;
    }

    /**
     * Ändert das Passwort eines Benutzers.
     *
     * @param token       Das JWT-Token des Benutzers.
     * @param oldPassword Das alte Passwort des Benutzers.
     * @param newPassword Das neue Passwort des Benutzers.
     * @return ResponseEntity mit einer Erfolgsmeldung oder Fehlermeldung und dem entsprechenden HTTP-Status.
     */
    public ResponseEntity<Object> changePassword(String token, String oldPassword, String newPassword) {
        try {
            // Entfernen Sie das "Bearer"-Präfix und alle möglichen Leerzeichen
            token = token.replace("Bearer ", "").trim();
            // Extrahieren der Benutzerkennung aus dem Token
            String username = jwtService.extractUsername(token);
            User existingUser = (User) loadUserByUsername(username);

            // Überprüfen, ob das alte Passwort korrekt ist
            if (!bCryptPasswordEncoder.matches(oldPassword, existingUser.getPassword())) {
                throw new IllegalStateException("Das alte Passwort ist nicht korrekt!");
            }
            // Das neue Passwort verschlüsseln und setzen
            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
            existingUser.setPassword(encodedPassword);

            // User speichern
            User savedUser = userRepository.save(existingUser);

            // Erfolgsnachricht zurückgeben
            String message = "Passwort erfolgreich geändert";
            return ResponseEntity.ok().body("{\"message\": \"" + message + "\"}");
        } catch (UserNotFoundException e) {
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        } catch (IllegalStateException e) {
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
}



