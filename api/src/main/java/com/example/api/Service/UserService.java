package com.example.api.Service;

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

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        // Instanz der Klasse erstellen und die Methode aufrufen

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    /**
     * Registriert einen User und gibt eine entsprechende JSON-Antwort zurück.
     *
     * @param user Der zu registrierende User.
     * @return ResponseEntity mit einer Erfolgsmeldung oder Fehlermeldung und dem entsprechenden HTTP-Status.
     */
    public ResponseEntity<?> userRegistration(User user) {
        try {
            // Überprüfen, ob die E-Mail-Adresse bereits vorhanden ist
            boolean userExists = userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent();

            // Wenn die E-Mail-Adresse bereits existiert, Exception auslösen
            if (userExists) {
                throw new IllegalStateException("E-Mail Adresse ist bereits vergeben!");
            }

            String encodedPassword = bCryptPasswordEncoder
                    .encode(user.getPassword());

            user.setPassword(encodedPassword);


            // das Registrierungsdatum auf das aktuelle Datum setzen
            user.setRegistrationDate(LocalDate.now());

            // Wenn alles i.O. ist, wird der User registriert
            User savedUser = userRepository.save(user);

            emailService.sendEmail(user.getEmail(),
                    "Willkommen im System",
                    "Hallo liebe HTW-Studentin, Sie haben sich erfolgreich registriert und können die Platform nun nutzen. Viel Freude dabei!");


            // Erfolgreiche Registrierung - User-Objekt zurückgeben
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

        } catch (IllegalStateException e) {
            // Exception abfangen und Fehler-JSON-Response zurückgeben
            String errorMessage = "{\"error\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }



    // weitere Methode, um auch die Attribute zu vergeben/speichern,
    // praktisch, wenn Attribute zb Profilbild automatisch von System erzeugt wird und
    // nicht alle Attribute vom Nutzer selbst eingeben werden
    /**
     * Registriert einen User anhand eines UserRequest-Objekts und gibt eine entsprechende JSON-Antwort zurück.
     *
     * @param userRequest Das UserRequest-Objekt mit den Benutzerdaten.
     * @return ResponseEntity mit einer Erfolgsmeldung oder Fehlermeldung und dem entsprechenden HTTP-Status.
     */
    public ResponseEntity<?> register(UserRequest userRequest){
        // greift vorher erstellte Methode zurück
        return userRegistration(new User(
                userRequest.getFirstname(),
                userRequest.getLastname(),
                userRequest.getEmail(),
                userRequest.getPassword(),
                null,
                Role.STUDENT,
                false,
                true
        )
        );
    }

    // Methode um User nach id zu laden
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

    // Methode um alle User zu laden
    /**
     * Lädt alle User aus der Datenbank.
     *
     * @return Eine Liste aller User.
     */
    public List<User> allUser() {
        return userRepository.findAll();
    }

    // Methode um Daten eine Userin zu aktualisieren
    /**
     * Aktualisiert die Daten eines Users anhand der angegebenen ID.
     *
     * @param id      Die ID des zu aktualisierenden Users.
     * @param newUser Die neuen Daten des Users.
     * @return ResponseEntity mit einer Erfolgsmeldung und dem HTTP-Status OK.
     * @throws UserNotFoundException Falls der User nicht gefunden wird.
     */
    public ResponseEntity<User> updateUser(Long id, User newUser) {

            // Externe Person anhand der ID finden
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            //existierende USerin mit neuen Daten updaten
            existingUser.setFirstName(newUser.getFirstName());
            existingUser.setLastName(newUser.getLastName());
            existingUser.setEmail(newUser.getEmail());
            User savedUser = userRepository.save(existingUser);

            /*
            // Response erstellen für den Erfolgsfall
            String message = "{\"User mit der id \"" + id + "\" wurde erfolgreich aktualisiert!\"";
            // eine ResponseEntity mit der Erfolgsmeldung und dem HTTP-Status OK wird zurückgegeben
            return ResponseEntity.ok(message);
            */
            return ResponseEntity.ok(savedUser);
    }

    //Methode zum Löschen eines Users
    /**
     * Löscht einen User anhand der angegebenen ID.
     *
     * @param id Die ID des zu löschenden Users.
     * @return ResponseEntity mit einer Erfolgsmeldung und dem HTTP-Status OK.
     * @throws UserNotFoundException Falls der User nicht gefunden wird.
     */
    public ResponseEntity<Object> deleteUser(Long id) {
        try {
            // den User anhand der ID im Repository zu finden
            // wenn der User nicht gefunden wird, wird eine Exception ausgelöst
            // und über die UserNotFoundException-Class behandelt
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            // Wenn der User gefunden wird, wird er aus dem Repository geloescht
            userRepository.delete(existingUser);

            String message = "{\"User mit der ID \"" + id + "\" erfolgreich gelöscht!\"}";
            // eine ResponseEntity mit der Erfolgsmeldung und dem HTTP-Status OK wird zurückgegeben
            return ResponseEntity.ok(message);
        }
        catch (UserNotFoundException e) {
            // Exception abfangen und Fehler-Response zurückgeben
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }


    // Methode für Login
    /**
     * Meldet einen User anhand der E-Mail-Adresse und des Passworts an und gibt eine entsprechende JSON-Antwort zurück.
     *
     * @param email       Die E-Mail-Adresse des Users.
     * @param password    Das Passwort des Users.
     * @return ResponseEntity mit einer Erfolgsmeldung oder Fehlermeldung und dem entsprechenden HTTP-Status.
     */
    public ResponseEntity<Object> signInUser(String email, String password) {
        try {
            // Authentifizierung des Benutzers durch die AuthenticationService
            AuthenticationResponse authenticationResponse = authenticationService.authenticate(
                    new AuthenticationRequest(email, password)
            );

            // Überprüfen, ob die Authentifizierung erfolgreich war
            if (authenticationResponse == null) {
                throw new IllegalStateException("Login war nicht erfolgreich! " +
                        "Email oder Passwort nicht korrekt!");
            }
            // JWT-Token in der Response zurückgeben
            return ResponseEntity.ok(authenticationResponse);

            //return ResponseEntity.ok(existingUser);

        } catch (BadCredentialsException e) {
            // Exception abfangen und Fehler-Response zurückgeben
            String errorMessage = "{ \"error\": \"Login war nicht erfolgreich! " +
                    "Email oder Passwort nicht korrekt!\" }";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
        catch (IllegalStateException e) {
            // Exception abfangen und Fehler-Response zurückgeben
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }


    //Methode für Logout
    public ResponseEntity<Object> signOut(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Überprüfen, ob der Benutzer ein gültiges JWT-Token im Header hat
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new IllegalStateException("Es ist kein Benutzer eingeloggt.");
            }

            // Token aus dem Authorization-Header extrahieren
            String token = authorizationHeader.substring(7);

            // Hier wird angenommen, dass die Methode isTokenValid implementiert ist und das Token überprüft
            if (!jwtService.isTokenValid(token, loadUserByUsername(jwtService.extractUsername(token))))
                throw new IllegalStateException("Das Token ist ungültig oder abgelaufen.");

            // Die Session des Benutzers ungültig machen
            // Dies ist notwendig, da der Benutzer sonst nicht ausgeloggt wird
            // und das JWT-Token weiterhin gültig ist
            // Die Methode invalidate() setzt das Ablaufdatum der Session auf die Vergangenheit
            // Dadurch wird die Session ungültig und der Benutzer wird ausgeloggt
            request.getSession().invalidate();

            // Das JWT-Token im Cookie löschen
            // Dazu wird ein neues Cookie erstellt, das den gleichen Namen wie das alte Cookie hat
            // und das Ablaufdatum auf die Vergangenheit setzt
            // Dadurch wird das Cookie ungültig und der Benutzer wird ausgeloggt
            // Das Cookie wird dem HttpServletResponse-Objekt hinzugefügt
            // Dadurch wird das Cookie an den Client gesendet
            // Der Client löscht das Cookie und der Benutzer wird ausgeloggt
            // Das Cookie wird auf dem Pfad "/" gesetzt
            // Dadurch wird das Cookie für alle URLs auf der Domain gültig
            Cookie cookie = new Cookie("jwtToken", null);
            cookie.setMaxAge(0); // Ablaufdatum auf null (Vergangenheit) setzen
            cookie.setPath("/"); // Der Pfad wird auf "/" gesetzt
            response.addCookie(cookie);

            String message = "Logout erfolgreich";
            return ResponseEntity.ok().body("{\"message\": \"" + message + "\"}");
        } catch (IllegalStateException e) {
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }

}



