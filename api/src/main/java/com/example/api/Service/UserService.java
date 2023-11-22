package com.example.api.Service;

import com.example.api.Entitys.Role;
import com.example.api.Entitys.User;
import com.example.api.Repository.UserRepository;
import com.example.api.Request.UserRequest;
import com.example.api.UserNotFound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String userRegistration(User user){
        // da es sein kann, dass Email Adresse schon im System vorhanden ist, wird boolean gesetzt
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
        // wenn Email-Adresse des Studenten schon in Datenbank, dann Exception
        if(userExists) {
            throw new IllegalMonitorStateException("E-Mail Adresse ist bereits vergeben");
        }
        userRepository.save(user);
        return "User wurde erfolgreich registriert.";

    }

    // weitere Methode, um auch die Attribute zu vergeben/speichern,
    // praktisch, wenn Attribute zb Profilbild automatisch von System erzeugt wird und
    // nicht alle Attribute vom Nutzer selbst eingeben werden

    public String register(UserRequest userRequest){
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
    public String updateUser(Long id, User newUser) {
        // Externe Person anhand der ID finden
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        //existierende USerin mit neuen Daten updaten
        existingUser.setFirstName(newUser.getFirstName());
        existingUser.setLastName(newUser.getLastName());
        existingUser.setEmail(newUser.getEmail());
        userRepository.save(existingUser);
        return "User mit der ID " + id + " erfolgreich aktualisiert!";
    }

    //Methode zum Löschen eines Users
    public String deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(existingUser);

        return "User mit der ID " + id + " erfolgreich gelöscht!";
    }

    // Methode für Login
    // Optional : User wird nur ausgegeben, wenn in der DB vorhanden
    public Optional<User> login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
