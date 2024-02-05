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
import com.example.api.NotFoundExceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * Service-Klasse für externe Personen
 */
@Service
public class ExternalService {

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


    /**
     * Konstruktor für die Service-Klasse für externe Personen
     *
     * @param externalRepository Die Repository-Klasse für externe Personen
     * @param bCryptPasswordEncoder Die Klasse für das Verschlüsseln von Passwörtern
     * @param bachelorSubjectRepository Die Repository-Klasse für BachelorSubjects
     * @param emailService Die Service-Klasse für das Versenden von E-Mails
     */
    public ExternalService(ExternalRepository externalRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           BachelorSubjectRepository bachelorSubjectRepository,
                           SpecialFieldRepository specialFieldRepository,
                           EmailService emailService) {
        this.externalRepository = externalRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.bachelorSubjectRepository = bachelorSubjectRepository;
        this.specialFieldRepository = specialFieldRepository;
        this.emailService = emailService;
    }


    /**
     * Methode für das Registrieren einer externen Person
     *
     * @param externalRequest Die Anfrage mit den Daten der externen Person
     * @param isAdmin Gibt an, ob die Registrierung von einem Admin durchgeführt wird
     * @return ResponseEntity mit den Daten der registrierten externen Person
     */
    @Transactional
    public ResponseEntity<Object> registration(ExternalRequest externalRequest, boolean isAdmin) {
        try {
            boolean externExists = externalRepository.findByEmail(externalRequest.getEmail()).isPresent();
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
                    null, // Specialfield initial auf null, da erst Id von external generiert werden muss
                    null  // BachelorSubjects initial auf null gesetzt
            );

            // Passwort verschlüsseln
            String encodedPassword = bCryptPasswordEncoder.encode(external.getPassword());
            external.setPassword(encodedPassword);

            // das Registrierungsdatum auf das aktuelle Datum setzen
            external.setRegistrationDate(LocalDate.now());

            External savedExternal = externalRepository.save(external);

            String firstName = externalRequest.getFirstName();
            String lastName = externalRequest.getLastName();
            String email = externalRequest.getEmail();

            if (isAdmin) {
                emailService.sendWelcomeEmailExternalByAdmin(email, firstName, lastName);
            } else {
                emailService.sendWelcomeEmailExternal(email, firstName, lastName);
            }

            // BachelorSubjects behandeln
            List<BachelorSubject> bachelorSubjects = externalRequest.getBachelorSubjects();
            List<SpecialField> specialFields = null;
            if (bachelorSubjects != null) {
                for (BachelorSubject subject : bachelorSubjects) {
                    // für jedes BachelorSubject die externe Person setzen
                    subject.setExternal(savedExternal);
                    System.out.println("BachelorSubject Title: " + subject.getTitle());
                    System.out.println("BachelorSubject BDescription: " + subject.getBDescription());
                }
                // dazugehörige BachelorSubjects speichern
                savedExternal.setBachelorSubjects(bachelorSubjectRepository.saveAll(bachelorSubjects));

                specialFields = externalRequest.getSpecialFields();
                if (specialFields != null) {

                    if (savedExternal.getSpecialFields() == null) {
                        savedExternal.setSpecialFields(new HashSet<>());
                    }

                    for (SpecialField field : specialFields) {

                        // bidirektionale Beziehung herstellen zwischen SpecialField und External
                        field.getExternal().add(savedExternal);
                        savedExternal.getSpecialFields().add(field);

                        specialFieldRepository.save(field);
                        externalRepository.save(savedExternal);
                    }
                }
            }
            ExternalDTO externalDTO = new ExternalDTO(savedExternal, specialFields, bachelorSubjects);

            return ResponseEntity.status(HttpStatus.CREATED).body(externalDTO);
        } catch (IllegalStateException e) {
            String errorMessage = "{\"error\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.status(400).body(errorMessage);
        }
    }


    /**
     * Methode um eine externe Person anhand der ID zu laden
     *
     * @param id Die ID der zu ladenden externen Person
     * @return ExternalDTO mit den Daten der externen Person
     * @throws UserNotFoundException wenn die externe Person nicht gefunden wurde
     */
    public ExternalDTO getExternal(Long id) {
        External external = externalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        List<BachelorSubject> bachelorSubjects = external.getBachelorSubjects();
        List<SpecialField> specialFields = new ArrayList<>(external.getSpecialFields());

        return new ExternalDTO(external, specialFields, bachelorSubjects);
    }


    /**
     * Methode um eine externe Person anhand der E-Mail zu laden
     *
     * @param email Die E-Mail der zu ladenden externen Person
     * @return ExternalDTO mit den Daten der externen Person
     * @throws UserNotFoundException wenn die externe Person nicht gefunden wurde
     */
    public ExternalDTO getExternalByEmail(String email) {
        External external = externalRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        List<BachelorSubject> bachelorSubjects = external.getBachelorSubjects();
        List<SpecialField> specialFields = new ArrayList<>(external.getSpecialFields());

        return new ExternalDTO(external, specialFields, bachelorSubjects);
    }


    /**
     * Methode um alle externen Personen (Zweitbetreuer*innen) zu laden
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


    /**
     * Methode um eine externe Person anhand der ID zu aktualisieren.
     *
     * @param id Die ID der zu aktualisierenden externen Person
     * @param externalRequest Die Anfrage mit den neuen Daten der externen Person
     * @return ResponseEntity mit den Daten der aktualisierten externen Person
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
