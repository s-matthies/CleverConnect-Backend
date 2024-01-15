package com.example.api.Service;

import com.example.api.Entitys.Role;
import com.example.api.Entitys.User;
import com.example.api.Repository.UserRepository;
import com.example.api.Request.UserRequest;
import com.example.api.UserNotFound.UserNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    /**
     * Konstruktor für die UserService-Klasse.
     *
     * @param userRepository        Das UserRepository-Objekt, das für den Zugriff auf die Datenbank verwendet wird.
     * @param bCryptPasswordEncoder Das BCryptPasswordEncoder-Objekt, das für die Verschlüsselung von Passwörtern
     *                              verwendet wird.
     * @param emailService          Das EmailService-Objekt, das für den Versand von E-Mails verwendet wird.
     */
    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
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
            return ResponseEntity.ok(savedUser);
            /*
            String message = "{\"User wurde erfolgreich registriert!\"}";
            return ResponseEntity.ok(message);
            */

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


    // Methode für Login
    /**
     * Meldet einen User anhand der E-Mail-Adresse und des Passworts an und gibt eine entsprechende JSON-Antwort zurück.
     *
     * @param email       Die E-Mail-Adresse des Users.
     * @param password    Das Passwort des Users.
     * @param httpSession Die HTTP-Session, in der der angemeldete User gespeichert wird.
     * @return ResponseEntity mit einer Erfolgsmeldung oder Fehlermeldung und dem entsprechenden HTTP-Status.
     */
    public ResponseEntity<Object> signInUser(String email, String password, HttpSession httpSession) {
        try {
            //
            User existingUser = userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new IllegalStateException("Login war nicht erfolgreich! " +
                            "Email oder Passwort nicht korrekt!"));

            // Passwort mit dem eingegebenen Passwort vergleichen
            boolean passwordMatches = bCryptPasswordEncoder.matches(password, existingUser.getPassword());

            if (!passwordMatches) {
                throw new IllegalStateException("Login war nicht erfolgreich! " +
                        "Email oder Passwort nicht korrekt!");
            }

            // User in der Session speichern
            httpSession.setAttribute("loggedInUser", existingUser);

            return ResponseEntity.ok(existingUser);

            /*
            String message = "User wurde erfolgreich angemeldet";
            // eine ResponseEntity mit der Erfolgsmeldung und dem HTTP-Status OK wird zurückgegeben
            return new ResponseEntity<>(message, HttpStatus.OK);
            */
        }
        catch (IllegalStateException e) {
            // Exception abfangen und Fehler-Response zurückgeben
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }

    //Methode für Logout
    /**
     * Beendet die Sitzung eines Benutzers und führt eine Abmeldung durch.
     *
     * @param httpSession Repräsentiert die Sitzung des Benutzers. Wird verwendet,
     *                    um benutzerspezifische Daten aus der Sitzung zu entfernen und die Sitzung zu invalidieren (ungültig zu machen).
     * @param request     Repräsentiert die HTTP-Anfrage des Benutzers. Wird verwendet, um
     *                    auf die Cookies zuzugreifen.
     * @param response    Repräsentiert die HTTP-Antwort, die zur Steuerung des
     *                    HTTP-Antwortverhaltens verwendet wird.
     * @return Eine ResponseEntity<Object> mit einer JSON-Antwort, die den Erfolg oder Fehler des Logout-Vorgangs angibt.
     *         Bei erfolgreichem Logout wird der HTTP-Status 200 OK zurückgegeben, andernfalls der HTTP-Status 401 Unauthorized.
     *         Die JSON-Antwort enthält eine Erfolgsmeldung oder eine Fehlermeldung, je nachdem, ob ein Benutzer eingeloggt war oder nicht.
     *         Beispiel für eine Erfolgsmeldung: {"message": "Logout erfolgreich"}
     *         Beispiel für eine Fehlermeldung: {"error": "Es ist kein Benutzer eingeloggt."}
     */
    public ResponseEntity<Object> signOut(HttpSession httpSession,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        try {
            // Überprüfen, ob ein Benutzer eingeloggt ist
            User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
            if (loggedInUser == null) {
                throw new IllegalStateException("Es ist kein User eingeloggt.");
            }

            // Lösche benutzerspezifische Daten aus der Sitzung
            httpSession.removeAttribute("loggedInUser");

            // Beende die aktuelle Sitzung (Session)
            httpSession.invalidate();

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("JSESSIONID")) {
                        cookie.setMaxAge(0); // Ablaufdatum auf null (Vergangenheit) setzen
                        cookie.setPath("/"); // der Pfad wird auf "/" gesetzt
                        response.addCookie(cookie);
                        //Dadurch wird der JSESSIONID-Cookie auf dem Client (Browser) gelöscht
                    }
                }
            }

            String message = "Logout erfolgreich";
            return ResponseEntity.ok().body("{\"message\": \"" + message + "\"}");
        }
        catch (IllegalStateException e) {
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }
}



