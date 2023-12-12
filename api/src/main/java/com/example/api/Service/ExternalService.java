package com.example.api.Service;

import com.example.api.Entitys.External;
import com.example.api.Entitys.Role;
import com.example.api.Repository.ExternalRepository;
import com.example.api.Request.ExternalRequest;
import com.example.api.UserNotFound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalService {

    //mit Repository verknüpfen, damit wir auf die DB zugreifen können
    @Autowired
    private final ExternalRepository externalRepository;

    @Autowired
    private EmailService emailService;

    //Konstruktor
    public ExternalService(ExternalRepository externalRepository) {
        this.externalRepository = externalRepository;
    }

    /*
     * User (hier Extern) soll sich mit seiner E-Mail-Adresse einloggen können.
     * Dafür muss die gewährleistet werden, dass die E-Mail-Adresse eindeutig ist.
     * Wir checken, ob in unserer DB die eingegebene E-Mail bereits vorhanden ist.
     * Über das Repository können wir unsere DB nach bestimmten Usern durchsuchen.
     */

    //SignUp-Methode erstellen
    public String registerExtern(External external) {
        //existiert student mit dieser email bereits in der DB?
        // im Repository findByMethode angelegt (-> E-Mail-Adresse muss eindeutig sein)
        boolean externExists = externalRepository.findByEmail(external.getEmail()).isPresent();

        //wenn E-Mail bereits existiert, dann Exception
        if (externExists) {
            throw new IllegalStateException(("Die E-Mail ist bereits vergeben!"));
        }

        externalRepository.save(external);

        emailService.sendEmail(external.getEmail(),
                    "Willkommen im System",
                    "Hallo, Sie haben sich erfolgreich registriert und können die Platform nun nutzen. Viel Freude dabei!");


        return "User*in wurde erfolgreich registriert!";
    }

    /*
     * wir erstellen eine Request: das was von uns an den Server weitergeben
     * werden soll, um eine Userin (Externe) zu erstellen
     */

    public String registration(ExternalRequest externalRequest) {
        return registerExtern(new External(
                externalRequest.getFirstName(),
                externalRequest.getLastName(),
                externalRequest.getEmail(),
                externalRequest.getPassword(),
                Role.EXTERN,
                false,
                true,
                externalRequest.getCompany(),
                externalRequest.getAvailability()
                )
        );
    }

    // Methode um Externe nach id zu laden
    public External getExternal(Long id) {
        return externalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // Methode um alle Externen zu laden
    public List<External> allExternal() {
        return externalRepository.findAll();
    }

    // Methode um Daten eine Userin zu aktualisieren
    public String updateExternal(Long id, External newUser) {
        // Externe Person anhand der ID finden
        External existingUser = externalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        //existierende Externe mit neuen Daten updaten
        existingUser.setFirstName(newUser.getFirstName());
        existingUser.setLastName(newUser.getLastName());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setAvailability(newUser.getAvailability());
        existingUser.setCompany(newUser.getCompany());
        externalRepository.save(existingUser);
        return "User*in mit der ID " + id + " erfolgreich aktualisiert!";
    }

    //Methode zum Löschen einer Userin/Externen
    public String deleteExternal(Long id) {
        External existingUser = externalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        externalRepository.delete(existingUser);

        return "User*in mit der ID " + id + " erfolgreich gelöscht!";
    }

    //Methode zum Login
    // Optional : Userin wird nur ausgegeben, wenn in der DB vorhanden
    public Optional<External> login(String email, String password) {
        Optional <External> external = externalRepository.findByEmailAndPassword(email, password);
        return external;
    }

}
