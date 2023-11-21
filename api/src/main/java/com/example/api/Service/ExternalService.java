package com.example.api.Service;

import com.example.api.Entitys.External;
import com.example.api.Repository.ExternalRepository;
import com.example.api.Request.ExternalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalService {

    //mit Repository verknüpfen, damit wir auf die DB zugreifen können
    @Autowired
    private final ExternalRepository externRepository;

    //Konstruktor
    public ExternalService(ExternalRepository externRepository) {
        this.externRepository = externRepository;
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
        boolean externExists = externRepository.findByEmail(external.getEmail()).isPresent();

        //wenn E-Mail bereits existiert, dann Exception
        if (externExists) {
            throw new IllegalStateException(("Die E-Mail ist bereits vergeben!"));
        }

        externRepository.save(external);
        return "User*in wurde erfolgreich registriert!";
    }

    /*
     * wir erstellen eine Request: das was von uns an den Server weitergeben
     * werden soll, um eine Userin (Externe) zu erstellen
     */

    public String registration(ExternalRequest externRequest) {
        return registerExtern(new External(
                externRequest.getFirstName(),
                externRequest.getLastName(),
                externRequest.getEmail(),
                externRequest.getPassword(),
                externRequest.getCompany(),
                externRequest.getAvailability()
                )
        );
    }
}
