package com.example.api.Service;

import com.example.api.Entitys.User;
import com.example.api.Repository.UserRepository;
import com.example.api.Request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // weitere Methode, um auch die  Attribute zu vergeben/speichern,
    // praktisch, wenn Attribute zb Profilbild automatisch von System erzeugt wird und
    // nicht alle Attribute vom Nutzer selbst eingeben werden

    public String register(UserRequest userRequest){
        // greift vorher erstellte Methode zurück
        return userRegistration(new User(
                // Eingabe der Attribute
                userRequest.getFirstname(),
                userRequest.getLastname(),
                userRequest.getEmail(),
                userRequest.getPassword()
        ));
    }

    // Optional : Userin wird nur ausgegeben, wenn in der DB vorhanden
    public Optional<User> login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
