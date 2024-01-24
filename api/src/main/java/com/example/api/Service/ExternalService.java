package com.example.api.Service;

import com.example.api.DTO.ExternalDTO;
import com.example.api.Entitys.BachelorSubject;
import com.example.api.Entitys.External;
import com.example.api.Entitys.Role;
import com.example.api.Entitys.SpecialField;
import com.example.api.Repository.BachelorSubjectRepository;
import com.example.api.Repository.ExternalRepository;
import com.example.api.Repository.SpecialFieldRepository;
import com.example.api.Request.ExternalRequest;
import com.example.api.UserNotFound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExternalService {

    //mit Repository verknüpfen, damit wir auf die DB zugreifen können
    @Autowired
    private final ExternalRepository externalRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private BachelorSubjectRepository bachelorSubjectRepository;

    @Autowired
    private SpecialFieldRepository specialFieldRepository;

    @Autowired
    private EmailService emailService;


    //Konstruktor
    public ExternalService(ExternalRepository externalRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           BachelorSubjectRepository bachelorSubjectRepository,
                           EmailService emailService) {
        this.externalRepository = externalRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.bachelorSubjectRepository = bachelorSubjectRepository;
        this.emailService = emailService;
    }

    /**
     * Methode für die Registrierung einer externen Person (Zweitbtreuer*in)
     *
     * @param externalRequest Die Anfrage mit den Daten der zu registrierenden Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der registrierten Person (Zweitbetreuer*in)
     * @throws IllegalStateException wenn die E-Mail bereits vergeben ist
     */
    public ResponseEntity<Object> registration(ExternalRequest externalRequest) {
        try {
            //existiert student mit dieser email bereits in der DB?
            boolean externExists = externalRepository.findByEmail(externalRequest.getEmail()).isPresent();

            //wenn E-Mail bereits existiert, dann Exception
            if (externExists) {
                return ResponseEntity.badRequest().body("{\"error\": \"Die E-Mail ist bereits vergeben!\"}");
            }

            External external = new External(
                    externalRequest.getFirstName(),
                    externalRequest.getLastName(),
                    externalRequest.getEmail(),
                    externalRequest.getPassword(),
                    null,
                    Role.EXTERN,
                    false,
                    true,
                    externalRequest.getCompany(),
                    externalRequest.getAvailabilityStart(),
                    externalRequest.getAvailabilityEnd(),
                    externalRequest.getDescription(),
                    null, // Specialfield ersteinmal null, da erst Id von external generiert werden muss
                    null  // Set BachelorSubjects to null initially
            );

            // Passwort verschlüsseln
            String encodedPassword = bCryptPasswordEncoder.encode(external.getPassword());
            external.setPassword(encodedPassword);

            // das Registrierungsdatum auf das aktuelle Datum setzen
            external.setRegistrationDate(LocalDate.now());

            External savedExternal = externalRepository.save(external);

            emailService.sendEmail(external.getEmail(),
                    "Willkommen im System",
                    "Hallo, Sie haben sich erfolgreich registriert und können die Plattform nun nutzen. " +
                            "Viel Freude dabei!");

            // BachelorSubjects behandeln
            List<BachelorSubject> bachelorSubjects = externalRequest.getBachelorSubjects();
            if (bachelorSubjects != null) {
                for (BachelorSubject subject : bachelorSubjects) {
                    // Set the External entity for each BachelorSubject
                    subject.setExternal(savedExternal);
                }
                // dazugehörige BachelorSubjects speichern
                savedExternal.setBachelorSubjects(bachelorSubjectRepository.saveAll(bachelorSubjects));

                List<SpecialField> specialFields = externalRequest.getSpecialFields();
                if (specialFields != null) {
                    // Ensure the specialFields list is initialized only once
                    if (savedExternal.getSpecialFields() == null) {
                        savedExternal.setSpecialFields(new HashSet<>());
                    }

                    for (SpecialField field : specialFields) {

                        // Establish the bidirectional relationship
                        field.getExternal().add(savedExternal);
                        savedExternal.getSpecialFields().add(field);

                        specialFieldRepository.save(field);
                        externalRepository.save(savedExternal);
                    }
                    //savedExternal.setSpecialFields(specialFieldRepository.saveAll(specialFields));
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedExternal);
        } catch (IllegalStateException e) {
            String errorMessage = "{\"error\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.status(400).body(errorMessage);
        }
    }


    // Methode um Externe nach id zu laden
    /**
     * Methode um eine externe Person (Zweitbetreuer*in) anhand der ID zu laden
     *
     * @param id Die ID der zu ladenden externen Person (Zweitbetreuer*in)
     * @return ExternalDTO mit den Daten der externen Person (Zweitbetreuer*in)
     * @throws UserNotFoundException wenn die externe Person (Zweitbetreuer*in) nicht gefunden wurde
     */
    public ExternalDTO getExternal(Long id) {
        External external = externalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        List<BachelorSubject> bachelorSubjects = external.getBachelorSubjects();
        List<SpecialField> specialFields = new ArrayList<>(external.getSpecialFields());

        return new ExternalDTO(external, specialFields, bachelorSubjects);
    }



    /**
     * Methode um alle externen Personen (Zweitbetreuer*innen) mit ihren jeweiligen Bachelorthemen und Fachgebieten zu laden
     *
     * @return Liste von ExternalDTOs mit den Daten aller externen Personen (Zweitbetreuer*innen)
     */
    public List<ExternalDTO> getAllExternalsWithSubjects() {
        List<External> allExternals = externalRepository.findAll();
        List<ExternalDTO> externalDTOs = new ArrayList<>();

        for (External external : allExternals) {
            List<BachelorSubject> bachelorSubjects = external.getBachelorSubjects();
            List<SpecialField> specialFields = new ArrayList<>(external.getSpecialFields());
            externalDTOs.add(new ExternalDTO(external, specialFields, bachelorSubjects));
        }
        return externalDTOs;
    }


    // Methode um Daten eine Userin zu aktualisieren
    /**
     * Methode um eine externe Person (Zweitbetreuer*in) anhand der ID zu aktualisieren
     *
     * @param id Die ID der zu aktualisierenden externen Person (Zweitbetreuer*in)
     * @param externalRequest Die Anfrage mit den neuen Daten der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der aktualisierten externen Person (Zweitbetreuer*in)
     * @throws UserNotFoundException wenn die externe Person (Zweitbetreuer*in) nicht gefunden wurde
     */
    public ResponseEntity<Object> updateExternal(Long id, ExternalRequest externalRequest) {
        try {
            // Externe Person anhand der ID finden
            External existingExternal = externalRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            // existierende Externe mit neuen Daten updaten
            existingExternal.setFirstName(externalRequest.getFirstName());
            existingExternal.setLastName(externalRequest.getLastName());
            existingExternal.setEmail(externalRequest.getEmail());
            existingExternal.setCompany(externalRequest.getCompany());
            existingExternal.setAvailabilityStart(externalRequest.getAvailabilityStart());
            existingExternal.setAvailabilityEnd(externalRequest.getAvailabilityEnd());
            existingExternal.setDescription(externalRequest.getDescription());

            // BachelorSubjects behandeln
            List<BachelorSubject> updatedSubjects = externalRequest.getBachelorSubjects();
            if (updatedSubjects != null) {
                existingExternal.getBachelorSubjects().clear();
                for (BachelorSubject subject : updatedSubjects) {
                    // für jedes BachelorSubject die externe Person setzen
                    subject.setExternal(existingExternal);
                }
                // dazugehörige BachelorSubjects speichern
                existingExternal.setBachelorSubjects(bachelorSubjectRepository.saveAll(updatedSubjects));
            }


            // SpecialFields behandeln
            List<SpecialField> updatedFields = externalRequest.getSpecialFields();
            if (updatedFields != null) {
                existingExternal.getSpecialFields().clear();
                for (SpecialField field : updatedFields) {
                    // bidirektionale Beziehung herstellen zwischen SpecialField und External
                    field.getExternal().add(existingExternal);
                    existingExternal.getSpecialFields().add(field);

                    specialFieldRepository.save(field);
                }
            }

            External savedExternal = externalRepository.save(existingExternal);

            return ResponseEntity.ok(savedExternal);
        } catch (IllegalStateException e) {
            String errorMessage = "{\"error\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.status(400).body(errorMessage);
        }
    }
}
