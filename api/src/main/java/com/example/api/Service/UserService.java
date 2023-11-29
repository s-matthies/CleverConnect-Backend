package com.example.api.Service;

import com.example.api.Entitys.Role;
import com.example.api.Entitys.User;
import com.example.api.Repository.UserRepository;
import com.example.api.Request.UserRequest;
import com.example.api.UserNotFound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    // Verknüpfung mit dem Repository, damit auf die Datenbank zugegriffen werden kann
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> userRegistration(User user) {
        try {
            // Überprüfen, ob die E-Mail-Adresse bereits vorhanden ist
            boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

            // Wenn die E-Mail-Adresse bereits existiert, Exception auslösen
            if (userExists) {
                throw new IllegalStateException("E-Mail Adresse ist bereits vergeben");
            }

            // Wenn alles i.O. ist, wird User registriert
            userRepository.save(user);

            // Erfolgreiche Registrierung - JSON-Response zurückgeben
            String message = "User wurde erfolgreich registriert!";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalStateException e) {
            // Exception abfangen und Fehler-JSON-Response zurückgeben
            String errorMessage = "{\"error\": \"" + e.getMessage() + "\"}";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    /*
    public String userRegistration(User user){
        // da es sein kann, dass Email Adresse schon im System vorhanden ist, wird boolean gesetzt
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
        // wenn Email-Adresse des Studenten schon in Datenbank, dann Exception
        if(userExists) {
            throw new IllegalStateException("E-Mail Adresse ist bereits vergeben");
        }
        userRepository.save(user);
        return "User wurde erfolgreich registriert.";
    }
    */

    // weitere Methode, um auch die Attribute zu vergeben/speichern,
    // praktisch, wenn Attribute zb Profilbild automatisch von System erzeugt wird und
    // nicht alle Attribute vom Nutzer selbst eingeben werden

    public ResponseEntity<String> register(UserRequest userRequest){
        // greift vorher erstellte Methode zurück
        return userRegistration(new User(
                // Eingabe der Attribute
                userRequest.getFirstname(),
                userRequest.getLastname(),
                userRequest.getEmail(),
                userRequest.getPassword(),
                Role.STUDENT,
                false,
                true
        )
        );
    }

    // Methode um User nach id zu laden
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // Methode um alle User zu laden
    public List<User> allUser() {
        return userRepository.findAll();
    }

    // Methode um Daten eine Userin zu aktualisieren
    public ResponseEntity<Object> updateUser(Long id, User newUser) {

            // Externe Person anhand der ID finden
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            //existierende USerin mit neuen Daten updaten
            existingUser.setFirstName(newUser.getFirstName());
            existingUser.setLastName(newUser.getLastName());
            existingUser.setEmail(newUser.getEmail());
            userRepository.save(existingUser);

            // Response erstellen für den Erfolgsfall
            String message = "User mit der id " + id + " wurde erfolgreich aktualisiert!";
            // eine ResponseEntity mit der Erfolgsmeldung und dem HTTP-Status OK wird zurückgegeben
            return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //Methode zum Löschen eines Users
    public ResponseEntity<Object> deleteUser(Long id) {

            // den User anhand der ID im Repository zu finden
            // wenn der User nicht gefunden wird, wird eine Exception ausgelöst
            // und über die UserNotFoundException-Class behandelt
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            // Wenn der User gefunden wird, wird er aus dem Repository geloescht
            userRepository.delete(existingUser);

            String message = "User mit der ID " + id + " erfolgreich gelöscht!";
            // eine ResponseEntity mit der Erfolgsmeldung und dem HTTP-Status OK wird zurückgegeben
            return new ResponseEntity<>(message, HttpStatus.OK);
        }


    // Methode für Login
    // Optional : User wird nur ausgegeben, wenn in der DB vorhanden
    public Optional<User> login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public ResponseEntity<Object> signInUser(String email, String password) {
        try {
        User existingUser = userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalStateException("Login war nicht erfolgreich! " +
                        "Email oder Passwort nicht korrekt!"));

            String message = "User wurde erfolgreich angemeldet";
            // eine ResponseEntity mit der Erfolgsmeldung und dem HTTP-Status OK wird zurückgegeben
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch (IllegalStateException e) {
            // Exception abfangen und Fehler-Response zurückgeben
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
}
