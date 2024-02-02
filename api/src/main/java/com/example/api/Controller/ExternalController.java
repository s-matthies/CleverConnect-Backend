package com.example.api.Controller;

import com.example.api.DTO.ExternalDTO;
import com.example.api.Request.ExternalRequest;
import com.example.api.Service.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller für die externen Personen (Zweitbetreuer*innen)
 */
@RestController
@RequestMapping("/external")
public class ExternalController {
    @Autowired
    private final ExternalService externalService;

    /**
     * Konstruktor für den ExternalController
     * @param externalService Der Service für die externen Personen (Zweitbetreuer*innen)
     */
    public ExternalController(ExternalService externalService) {
        this.externalService = externalService;
    }


    /**
     * Methode für das Registrieren einer externen Person (Zweitbetreuer*in)
     *
     * @param externalRequest Die Daten der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der registrierten externen Person (Zweitbetreuer*in)
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody ExternalRequest externalRequest) {
        return externalService.registration(externalRequest);
    }

    /**
     * Methode für das Laden aller externen Personen (Zweitbetreuer*innen)
     *
     * @return ResponseEntity mit den Daten aller externen Personen (Zweitbetreuer*innen)
     */
    @GetMapping("/load")
    public ResponseEntity<List<ExternalDTO>> allExternals() {
        List<ExternalDTO> externalDTOS = externalService.getAllExternalsWithSubjects();
        return ResponseEntity.ok(externalDTOS);
    }

    /**
     * Methode für das Laden einer externen Person (Zweitbetreuer*in) per ID
     *
     * @param id Die ID der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der externen Person (Zweitbetreuer*in)
     */
    @GetMapping("/load/{id}")
    public ResponseEntity<ExternalDTO> getExternal(@PathVariable Long id) {
        ExternalDTO externalDTO = externalService.getExternal(id);
        return ResponseEntity.ok(externalDTO);
    }

    /**
     * Methode für das Laden einer externen Person (Zweitbetreuer*in) per E-Mail
     *
     * @param email Die E-Mail-Adresse der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der externen Person (Zweitbetreuer*in)
     */
    @GetMapping("/loadByEmail")
    public ResponseEntity<ExternalDTO> getExternalByEmail(@RequestParam String email) {
        ExternalDTO externalDTO = externalService.getExternalByEmail(email);
        return ResponseEntity.ok(externalDTO);
    }

    /**
     * Methode für das Löschen einer externen Person (Zweitbetreuer*in) per ID
     *
     * @param id Die ID der externen Person (Zweitbetreuer*in)
     * @param externalRequest Die Daten der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit einer Bestätigung
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateExternal (@PathVariable Long id,
                                                  @RequestBody ExternalRequest externalRequest) {
        return externalService.updateExternal(id, externalRequest);
    }
    
}



